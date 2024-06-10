package io.github.yonigev.sfsm.trigger

/**
 * A trigger message or event that is sent to the [io.github.yonigev.sfsm.StateMachine] to transition between states.
 * T is the ID type of the trigger
 */
interface Trigger<T> {
    val id: T

    companion object {
        fun <T> ofId(id: T): Trigger<T> {
            return object : Trigger<T> {
                override val id: T
                    get() = id
            }
        }
    }
}
