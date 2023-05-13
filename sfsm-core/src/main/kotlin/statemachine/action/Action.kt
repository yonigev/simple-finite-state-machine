package statemachine.action

/**
 * Defines an Action that will run (or act) when it is scheduled to, Typically after a state machine
 * state change.
 */
interface Action<S, T> {
    fun act()
}
