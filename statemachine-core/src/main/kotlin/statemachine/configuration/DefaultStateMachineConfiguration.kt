package statemachine.configuration

import org.slf4j.LoggerFactory
import statemachine.configuration.state.DefaultStatesConfiguration
import statemachine.configuration.state.StatesConfiguration
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.state.State
import statemachine.transition.TransitionMap

/**
 * Defines and validates a State Machine's configuration
 * Contains all necessary properties to build a [statemachine.StateMachine]
 */
open class DefaultStateMachineConfiguration<S, T> : StateMachineConfiguration<S, T> {
    private val statesConfiguration = DefaultStatesConfiguration<S, T>()
    private val transitionsConfiguration = DefaultTransitionsConfiguration<S, T>()
    private val log = LoggerFactory.getLogger(this.javaClass)

    private var initialState: State<S>? = null
    override lateinit var states: Set<State<S>>
    override lateinit var transitionMap: TransitionMap<S, T>
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
        this.states = statesConfiguration.getStates()
        this.initialState = statesConfiguration.getStates().first { State.PseudoStateType.INITIAL == it.getType() }
        validateTransitions()
        this.transitionMap = TransitionMap(transitionsConfiguration.getTransitions())
        processed = true
    }

    /**
     * Validate the states configuration
     */
    private fun validateStates() {
        val states = statesConfiguration.getStates()
        val initialStates = states.filter { State.PseudoStateType.INITIAL == it.getType() }
        val endStates = states.filter { State.PseudoStateType.TERMINAL == it.getType() }

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
        val transitions = transitionsConfiguration.getTransitions()
        val stateIds = this.states.map { it.getId() }
        val statesMap: Map<S, State<S>> = states.associateBy { it.getId() }

        val transitionStates: Set<S> = transitions.map { setOf(it.source, it.target) }.flatten().toSet()
        if (transitionStates != stateIds && (transitionStates + stateIds).size > states.size) {
            "Some transition states: $transitionStates are not defined in the states set: $states".let {
                throw StateMachineConfigurationException(
                    it,
                )
            }
        }

        // Validate transition with a CHOICE state source
        for (choiceState in states.filter { it.getType() == State.PseudoStateType.CHOICE }) {
            val choiceSourceTransitions = transitions.filter { it.source == choiceState.getId() }
            if (choiceSourceTransitions.size < 2) {
                throw StateMachineConfigurationException("Choice state with $choiceSourceTransitions.size outgoing transitions")
            }
        }

        // Validate transition with a non-choice state source
        for (nonChoiceStateTransition in transitions.filter { statesMap[it.source]!!.getType() != State.PseudoStateType.CHOICE }) {
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
