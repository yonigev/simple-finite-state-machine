package statemachine.trigger

/**
 * A trigger message or event that is sent to the [statemachine.StateMachine] to transition between states.
 */
interface Trigger<T> {
    fun getTriggerId(): T
    companion object {
        fun <S, T> ofId(id: T): Trigger<T> {
            return object : Trigger<T> {
                override fun getTriggerId(): T {
                    return id
                }
            }
        }
    }
}
