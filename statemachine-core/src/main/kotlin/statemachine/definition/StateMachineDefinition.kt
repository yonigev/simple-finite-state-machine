package statemachine.definition

import statemachine.definition.state.StatesDefinition
import statemachine.definition.transition.TransitionsDefinition
import statemachine.state.State
import statemachine.transition.Transition

/**
 * This class is used to define a state machine's states and transitions
 * this must be [processed] before creating a state machine.
 */
interface StateMachineDefinition<S, T> {
    var processed: Boolean
    var states: Set<State<S, T>>
    var transitions: Set<Transition<S, T>>

    /**
     * Returns a [StatesDefinition]
     * Override or call this method to define state machine states.
     */
    fun defineStates(): StatesDefinition<S, T>

    /**
     * Returns a [TransitionsDefinition]
     * Override or call this method to define state machine [statemachine.transition.Transition].
     */
    fun defineTransitions(): TransitionsDefinition<S, T>

    /**
     * Process validate and finalize the defined [StatesDefinition] and [TransitionsDefinition]
     */
    fun process()
}
