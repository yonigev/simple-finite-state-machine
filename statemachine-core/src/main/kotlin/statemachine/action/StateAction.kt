package statemachine.action

import statemachine.context.StateMachineContext

/**
 * An Action that will act either after entering a particular state or before exiting one
 */
interface StateAction<S, T> {
    fun act(stateMachineContext: StateMachineContext<S, T>)

    companion object {
        /**
         * Convenience function for creating a basic [StateAction] instance
         */
        @JvmStatic
        fun <S, T> create(f: (StateMachineContext<S, T>) -> Unit): StateAction<S, T> {
            return object : StateAction<S, T> {
                override fun act(stateMachineContext: StateMachineContext<S, T>) {
                    f.invoke(stateMachineContext)
                }
            }
        }
    }
}
