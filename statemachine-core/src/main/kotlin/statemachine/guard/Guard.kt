package statemachine.guard

import statemachine.transition.TransitionContext

/**
 * A Guard is in charge of allowing / dis-allowing a state transition. A transition will occur if
 * evaluate() returns true
 */
interface Guard<S, T> {
    fun transition(transitionContext: TransitionContext<S, T>): Boolean

    companion object {
        fun <S, T> ofPredicate(predicate: () -> Boolean): Guard<S, T> {
            return object : Guard<S, T> {
                override fun transition(transitionContext: TransitionContext<S, T>): Boolean {
                    return predicate()
                }
            }
        }
    }
}
