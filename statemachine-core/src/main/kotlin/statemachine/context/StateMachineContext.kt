package statemachine.context

import statemachine.state.State

/**
 * Represents the more extended state of the state machine i.e. the state itself and the additional
 * properties that define it
 */
interface StateMachineContext<S, T> {
    val state: State<S>
    fun transitionToState(state: State<S>): State<S>
    fun getProperty(key: Any): Any
    fun getPropertyOrDefault(key: Any, default: Any):  Any
    fun setProperty(key: Any, value: Any): Any
}
