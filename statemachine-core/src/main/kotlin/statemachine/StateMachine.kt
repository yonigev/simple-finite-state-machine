package statemachine

import statemachine.state.State
import statemachine.trigger.Trigger

/** Defines a basic State Machine S and T stand for State and Trigger types, respectively */
interface StateMachine<S, T> {
    val id: String
    val state: State<S>

    /** Trigger a state machine to run */
    fun trigger(trigger: Trigger<T>): State<S>
    fun start()
    fun stop()
}
