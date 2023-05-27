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
        this.initialState = statesConfiguration.getStates().first { State.Type.INITIAL == it.getType() }
        validateTransitions()
        this.transitionMap = TransitionMap(transitionsConfiguration.getTransitions())
        processed = true
    }

    /**
     * Validate that the states configuration is valid
     */
    private fun validateStates() {
        val states = statesConfiguration.getStates()
        val initialStates = states.filter { State.Type.INITIAL == it.getType() }
        val endStates = states.filter { State.Type.TERMINAL == it.getType() }

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
     * Validate that the transitions configuration is valid
     */
    private fun validateTransitions() {
        val transitions = transitionsConfiguration.getTransitions()
        val transitionStates: Set<S> = transitions.map { setOf(it.source, it.target) }.flatten().toHashSet()
        val stateIds = this.states.map { it.getId() }

        if (transitions.firstOrNull { it.source == it.target } != null) {
            throw StateMachineConfigurationException("Simple Finite State Machine does not currently support self transitions.")
        }

        if (transitionStates != stateIds && (transitionStates + stateIds).size > states.size) {
            "Some transition states: $transitionStates are not defined in the states set: $states".let {
                throw StateMachineConfigurationException(
                    it,
                )
            }
        }
    }
}
