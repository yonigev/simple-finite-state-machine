package statemachine.action

import statemachine.transition.TransitionContext

/**
 * Defines an Action that will act when after a state transition.
 */
interface Action<S, T> {
    fun act(transitionContext: TransitionContext<S, T>)

    companion object {
        /**
         * Convenience function for creating a basic [Action] instance
         */
        @JvmStatic
        fun <S, T> createAction(f: (TransitionContext<S, T>) -> Unit): Action<S, T> {
            return object : Action<S, T> {
                override fun act(transitionContext: TransitionContext<S, T>) {
                    f.invoke(transitionContext)
                }
            }
        }
    }
}
