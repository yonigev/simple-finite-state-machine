package statemachine

/**
 * Defines a basic Simple State Machine
 */
interface StateMachine<S, T> {
    fun getId(): String
    fun trigger(trigger: T)
    fun start()
    fun stop()
    fun getState(): S
}
