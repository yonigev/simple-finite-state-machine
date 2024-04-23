package statemachine.configuration

import org.slf4j.LoggerFactory
import statemachine.configuration.state.DefaultStatesConfiguration
import statemachine.configuration.state.StatesConfiguration
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.state.State
import statemachine.transition.Transition

/**
 * Defines and validates a State Machine's configuration
 * Contains all necessary properties to build a [statemachine.StateMachine]
 */
open class DefaultStateMachineConfiguration<S, T> : StateMachineConfiguration<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val statesConfiguration = DefaultStatesConfiguration<S, T>()
    private val transitionsConfiguration = DefaultTransitionsConfiguration<S, T>()

    override lateinit var states: Set<State<S, T>>
    override lateinit var transitions: Set<Transition<S, T>>
    override var processed: Boolean = false

    override fun configureStates(): StatesConfiguration<S, T> {
        if (processed) {
            "State Machine Configuration already processed".also {
                log.error(it)
                throw StateMachineConfigurationException(it)
            }
        }
        return statesConfiguration
    }

    override fun configureTransitions(): TransitionsConfiguration<S, T> {
        if (processed) {
            "State Machine Configuration already processed".also {
                log.error(it)
                throw StateMachineConfigurationException(it)
            }
        }
        return transitionsConfiguration
    }

    override fun process() {
        configureStates()
        configureTransitions()
        validateStates()
        validateTransitions()
        this.states = statesConfiguration.getStates()
        this.transitions = transitionsConfiguration.getTransitions()
        processed = true
    }

    /**
     * Validate the states configuration
     */
    private fun validateStates() {
        val initialStates = statesConfiguration.getStates().filter { State.PseudoStateType.INITIAL == it.type }
        val endStates = statesConfiguration.getStates().filter { State.PseudoStateType.TERMINAL == it.type }

        // Validate there is 1 INITIAL state
        if (initialStates.size != 1) {
            "Invalid number of INIITIAL states!: $initialStates".also {
                log.error(it); throw StateMachineConfigurationException(it)
            }
        }

        // Validate there is at least 1 END state
        if (endStates.isEmpty()) {
            "Invalid number of END states!: $endStates".also {
                log.error(it); throw StateMachineConfigurationException(it)
            }
        }
    }

    /**
     * Validate the transitions configuration
     */
    private fun validateTransitions() {
        val states = statesConfiguration.getStates()
        val transitions = transitionsConfiguration.getTransitions()
        val stateIds = states.map { it.id }
        val statesMap = states.associateBy { it.id }

        val transitionStates: Set<S> = transitions.map { setOf(it.source, it.target) }.flatten().toSet()
        if (transitionStates != stateIds && (transitionStates + stateIds).size > states.size) {
            "Some transition states: $transitionStates are not defined in the states set: $states".let {
                throw StateMachineConfigurationException(
                    it,
                )
            }
        }

        // Validate transition with a CHOICE state source
        for (choiceState in states.filter { it.type == State.PseudoStateType.CHOICE }) {
            val choiceSourceTransitions = transitions.filter { it.source == choiceState.id }
            if (choiceSourceTransitions.size < 2) {
                throw StateMachineConfigurationException("Choice state with $choiceSourceTransitions.size outgoing transitions")
            }
        }

        // Validate transition with a non-choice state source
        for (nonChoiceStateTransition in transitions.filter { statesMap[it.source]!!.type != State.PseudoStateType.CHOICE }) {
            val similarTransitions = transitions.filter {
                it.source == nonChoiceStateTransition.source &&
                    it.trigger == nonChoiceStateTransition.trigger
            }
            if (similarTransitions.size > 1) {
                throw StateMachineConfigurationException(
                    "Found multiple transitions with non-choice source state: ${nonChoiceStateTransition.source} " +
                        "and trigger: ${nonChoiceStateTransition.trigger}",
                )
            }
        }
    }
}
