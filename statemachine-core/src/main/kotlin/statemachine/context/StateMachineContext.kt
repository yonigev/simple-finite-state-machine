package statemachine.context

import statemachine.state.State

/**
 * Represents the context in which the [statemachine.StateMachine] runs in, including the machine's specific [State]
 * along with additional properties that define that state.
 */
sealed interface StateMachineContext<S, T> {
    fun getId(): String
    fun getState(): State<S, T>
    fun transitionToState(state: State<S, T>)
    fun getProperty(key: Any): Any
    fun getPropertyOrDefault(key: Any, default: Any): Any
    fun setProperty(key: Any, value: Any): Any
}
