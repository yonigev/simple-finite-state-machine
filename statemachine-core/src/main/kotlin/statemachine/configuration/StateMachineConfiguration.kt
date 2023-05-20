package statemachine.configuration

import statemachine.configuration.state.StatesConfiguration
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.state.State
import statemachine.transition.TransitionMap

/**
 * This class is used to configure a state machine's states and transitions
 * this must be [finalized] before creating a state machine.
 */
interface StateMachineConfiguration<S, T> {
    var finalized: Boolean
    var states: Set<State<S>>
    var transitionMap: TransitionMap<S, T>

    /**
     * Return a states configurator
     */
    fun configureStates(): StatesConfiguration<S, T>

    /**
     * Return a transition configurator
     */
    fun configureTransitions(): TransitionsConfiguration<S, T>

    /**
     * Finalize the configuration by validating the [StatesConfiguration] and [TransitionsConfiguration]
     * and instantiating the [states] and [transitionMap]
     */
    fun finalize()
}
