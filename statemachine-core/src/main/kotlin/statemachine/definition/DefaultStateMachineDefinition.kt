package statemachine.definition

import org.slf4j.LoggerFactory
import statemachine.definition.state.DefaultStatesDefinition
import statemachine.definition.state.StatesDefinition
import statemachine.definition.transition.TransitionsDefinition
import statemachine.state.State
import statemachine.transition.Transition

/**
 * Defines and validates a State Machine
 * Contains all necessary properties to build a [statemachine.StateMachine]
 */
open class DefaultStateMachineDefinition<S, T> : StateMachineDefinition<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val statesDefinition = DefaultStatesDefinition<S, T>()
    private val transitionsDefinition = TransitionsDefinition<S, T>()

    override lateinit var states: Set<State<S, T>>
    override lateinit var transitions: Set<Transition<S, T>>
    override var processed: Boolean = false

    override fun defineStates(): StatesDefinition<S, T> {
        if (processed) {
            "State Machine Definition already processed".also {
                log.error(it)
                throw StateMachineDefinitionException(it)
            }
        }
        return statesDefinition
    }

    override fun defineTransitions(): TransitionsDefinition<S, T> {
        if (processed) {
            "State Machine definition already processed".also {
                log.error(it)
                throw StateMachineDefinitionException(it)
            }
        }
        return transitionsDefinition
    }

    override fun process() {
        defineStates()
        defineTransitions()
        validateStates()
        validateTransitions()
        this.states = statesDefinition.getStates()
        this.transitions = transitionsDefinition.getTransitions()
        processed = true
    }

    /**
     * Validate the states definition
     */
    private fun validateStates() {
        val initialStates = statesDefinition.getStates().filter { State.PseudoStateType.INITIAL == it.type }
        val endStates = statesDefinition.getStates().filter { State.PseudoStateType.TERMINAL == it.type }

        // Validate there is 1 INITIAL state
        if (initialStates.size != 1) {
            "Invalid number of INIITIAL states!: $initialStates".also {
                log.error(it); throw StateMachineDefinitionException(it)
            }
        }

        // Validate there is at least 1 END state
        if (endStates.isEmpty()) {
            "Invalid number of END states!: $endStates".also {
                log.error(it); throw StateMachineDefinitionException(it)
            }
        }
    }

    /**
     * Validate the transitions definition
     */
    private fun validateTransitions() {
        val states = statesDefinition.getStates()
        val transitions = transitionsDefinition.getTransitions()
        val stateIds = states.map { it.id }
        val statesMap = states.associateBy { it.id }

        val transitionStates: Set<S> = transitions.map { setOf(it.source, it.target) }.flatten().toSet()
        if (transitionStates != stateIds && (transitionStates + stateIds).size > states.size) {
            "Some transition states: $transitionStates are not defined in the states set: $states".let {
                throw StateMachineDefinitionException(
                    it,
                )
            }
        }

        // Validate transition with a CHOICE state source
        for (choiceState in states.filter { it.type == State.PseudoStateType.CHOICE }) {
            val choiceSourceTransitions = transitions.filter { it.source == choiceState.id }
            if (choiceSourceTransitions.size < 2) {
                throw StateMachineDefinitionException("Choice state with $choiceSourceTransitions.size outgoing transitions")
            }
        }

        // Validate transition with a non-choice state source
        for (nonChoiceStateTransition in transitions.filter { statesMap[it.source]!!.type != State.PseudoStateType.CHOICE }) {
            val similarTransitions = transitions.filter {
                it.source == nonChoiceStateTransition.source &&
                    it.trigger == nonChoiceStateTransition.trigger
            }
            if (similarTransitions.size > 1) {
                throw StateMachineDefinitionException(
                    "Found multiple transitions with non-choice source state: ${nonChoiceStateTransition.source} " +
                        "and trigger: ${nonChoiceStateTransition.trigger}",
                )
            }
        }
    }
}
