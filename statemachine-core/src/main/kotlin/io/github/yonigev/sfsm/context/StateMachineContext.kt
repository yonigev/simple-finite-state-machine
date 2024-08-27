package io.github.yonigev.sfsm.context

import io.github.yonigev.sfsm.state.State

/**
 * Represents the context in which the [io.github.yonigev.sfsm.StateMachine] runs in, including the machine's current [State]
 * along with additional properties that define that state.
 */
sealed interface StateMachineContext<S, T> {
    val id: String
    var state: State<S, T>
    fun getProperty(key: Any): Any?
    fun getPropertyOrDefault(key: Any, default: Any): Any
    fun setProperty(key: Any, value: Any): Any
}
