package statemachine.transition

import statemachine.context.StateMachineContext
import statemachine.trigger.Trigger

/** A snapshot of the state machine containing necessary data to evaluate a transition. */
interface TransitionContext<S, T> {
    val stateMachineContext: StateMachineContext<S, T>
    val transition: Transition<S, T>
    val trigger: Trigger<T>?
}
