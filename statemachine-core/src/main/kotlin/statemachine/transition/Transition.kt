package statemachine.transition

import statemachine.action.TransitionAction
import statemachine.guard.Guard

/**
 * Defines the conditions required for a [statemachine.StateMachine] to transition from state [source] to state [target]
 * if the machine receives the defined [trigger] (or the trigger is null) - the transition will happen only if the [guard]
 * allows it. if it does - the defined [actions] will run.
 */
interface Transition<S, T> {
    val source: S
    val target: S
    val trigger: T?
    val guard: Guard<S, T>
    val actions: Iterable<TransitionAction<S, T>>
}
