package statemachine.guard

import statemachine.transition.TransitionContext

/**
 * A Guard is in charge of allowing or refusing a state transition.
 * A transition will occur if allow() returns true.
 */
interface Guard<S, T> {
    fun allow(transitionContext: TransitionContext<S, T>): Boolean

    companion object {
        /**
         * Create a [statemachine.transition.Transition] guard, based on the provided predicate.
         */
        fun <S, T> ofPredicate(predicate: () -> Boolean): Guard<S, T> {
            return object : Guard<S, T> {
                override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
                    return predicate()
                }
            }
        }
    }
}
