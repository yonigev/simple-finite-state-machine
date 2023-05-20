package statemachine.configuration

import statemachine.configuration.state.StatesConfiguration
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.state.State
import statemachine.transition.TransitionMap

/**
 * This class is used to configure a state machine's states and transitions
 * this must be [processed] before creating a state machine.
 */
interface StateMachineConfiguration<S, T> {
    var processed: Boolean
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
     * Process and finalize the configuration by validating the [StatesConfiguration] and [TransitionsConfiguration]
     * and instantiating the [states] and [transitionMap]
     */
    fun process()
}
