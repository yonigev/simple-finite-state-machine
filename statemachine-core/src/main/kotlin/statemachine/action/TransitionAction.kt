package statemachine.action

import statemachine.transition.TransitionContext

/**
 * Defines an Action that will act after a state transition.
 */
interface TransitionAction<S, T> {
    fun act(transitionContext: TransitionContext<S, T>)

    companion object {
        /**
         * Convenience function for creating a basic [TransitionAction] instance
         */
        @JvmStatic
        fun <S, T> create(f: (TransitionContext<S, T>) -> Unit): TransitionAction<S, T> {
            return object : TransitionAction<S, T> {
                override fun act(transitionContext: TransitionContext<S, T>) {
                    f.invoke(transitionContext)
                }
            }
        }
    }
}
