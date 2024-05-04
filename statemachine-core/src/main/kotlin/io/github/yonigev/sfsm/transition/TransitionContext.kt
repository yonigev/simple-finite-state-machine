package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.trigger.Trigger

/** A snapshot of the state machine containing necessary data to decide if a transition should occur. */
interface TransitionContext<S, T> {
    val stateMachineContext: StateMachineContext<S, T>
    val transition: Transition<S, T>
    val trigger: Trigger<T>?
}
