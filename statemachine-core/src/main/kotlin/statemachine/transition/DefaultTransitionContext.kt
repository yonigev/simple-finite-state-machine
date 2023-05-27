package statemachine.transition

import statemachine.context.StateMachineContext
import statemachine.trigger.Trigger

open class DefaultTransitionContext<S, T>(
    override val stateMachineContext: StateMachineContext<S, T>,
    override val transition: Transition<S, T>,
    override val trigger: Trigger<T>?,
) : TransitionContext<S, T>
