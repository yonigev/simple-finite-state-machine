package statemachine.transition

import statemachine.context.StateMachineContext

class DefaultTransitionContext<S, T>(
    override val stateMachineContext: StateMachineContext<S, T>,
    override val transition: Transition<S, T>,
) : TransitionContext<S, T>
