package statemachine.action

/**
 * Defines an Action that will act when after a state transition.
 */
interface Action<S, T> {
    fun act()
}

/**
 * Convenience function for creating a basic [Action] instance
 */
fun <S, T> createAction(f: () -> Unit): Action<S, T> {
    return object : Action<S, T> {
        override fun act() {
            f.invoke()
        }
    }
}
