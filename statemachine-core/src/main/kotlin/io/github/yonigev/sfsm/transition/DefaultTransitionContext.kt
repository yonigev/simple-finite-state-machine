package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.trigger.Trigger

open class DefaultTransitionContext<S, T>(
    override val stateMachineContext: StateMachineContext<S, T>,
    override val transition: Transition<S, T>,
    override val trigger: Trigger<T>?,
) : TransitionContext<S, T>
