package statemachine.transition

import statemachine.context.StateMachineContext

open class DefaultTransitionContext<S, T>(
    override val stateMachineContext: StateMachineContext<S, T>,
    override val transition: Transition<S, T>,
) : TransitionContext<S, T>
