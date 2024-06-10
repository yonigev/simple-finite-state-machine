package io.github.yonigev.sfsm.guard

import io.github.yonigev.sfsm.transition.TransitionContext

/**
 * A Guard is in charge of allowing or refusing a state transition.
 * A transition will occur if allow() returns true.
 */
interface Guard<S, T> {
    fun allow(transitionContext: TransitionContext<S, T>): Boolean

    companion object {
        /**
         * Create a [io.github.yonigev.sfsm.transition.Transition] guard, based on the provided predicate and transition context.
         */
        fun <S, T> ofPredicate(predicate: () -> Boolean): Guard<S, T> {
            return object : Guard<S, T> {
                override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
                    return predicate()
                }
            }
        }

        /**
         * Convenience function to create a [io.github.yonigev.sfsm.transition.Transition] guard, based on the provided predicate.
         */
        fun <S, T> ofContextualPredicate(predicate: (TransitionContext<S, T>) -> Boolean): Guard<S, T> {
            return object : Guard<S, T> {
                override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
                    return predicate(transitionContext)
                }
            }
        }
    }
}
