package statemachine.action

/**
 * Defines an Action that will act when after a state transition.
 */
interface Action<S, T> {
    fun act()

    companion object {
        /**
         * Convenience function for creating a basic [Action] instance
         */
        @JvmStatic
        fun <S, T> createAction(f: Function0<Unit>): Action<S, T> {
            return object : Action<S, T> {
                override fun act() {
                    f.invoke()
                }
            }
        }
    }
}
