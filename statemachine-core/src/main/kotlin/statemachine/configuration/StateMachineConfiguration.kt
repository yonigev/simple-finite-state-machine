package statemachine.configuration

import statemachine.configuration.state.StateDefinition
import statemachine.configuration.state.StatesConfiguration
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.transition.TransitionMap

/**
 * This class is used to configure a state machine's states and transitions
 * this must be [processed] before creating a state machine.
 */
interface StateMachineConfiguration<S, T> {
    var processed: Boolean
    var stateDefinitions: List<StateDefinition<S, T>>
    var transitionMap: TransitionMap<S, T>

    /**
     * Returns a [StatesConfiguration]
     * Override or call this method to configure state machine states.
     */
    fun configureStates(): StatesConfiguration<S, T>

    /**
     * Returns a [TransitionsConfiguration]
     * Override or call this method to configure state machine [statemachine.transition.Transition].
     */
    fun configureTransitions(): TransitionsConfiguration<S, T>

    /**
     * Process validate and finalize the configured [StatesConfiguration] and [TransitionsConfiguration]
     */
    fun process()
}
