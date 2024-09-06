package io.github.yonigev.sfsm.action

import io.github.yonigev.sfsm.transition.TransitionContext

/**
 * An Action that will act after a state transition.
 */
fun interface TransitionAction<S, T> {
    fun act(transitionContext: TransitionContext<S, T>)
}
