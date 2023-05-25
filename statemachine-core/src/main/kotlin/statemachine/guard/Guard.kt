package statemachine.guard

import statemachine.transition.TransitionContext

/**
 * A Guard is in charge of allowing / dis-allowing a state transition. A transition will occur if
 * evaluate() returns true
 */
interface Guard<S, T> {
    fun evaluate(stateMachineContext: TransitionContext<S, T>): Boolean

    companion object {
        fun <S, T> createGuard(predicate: () -> Boolean): Guard<S, T> {
            return object : Guard<S, T> {
                override fun evaluate(stateMachineContext: TransitionContext<S, T>): Boolean {
                    return predicate()
                }
            }
        }
    }
}
