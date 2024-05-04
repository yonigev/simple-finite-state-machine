package io.github.yonigev.sfsm

import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.trigger.Trigger

/** Defines a basic State Machine.
 * S and T stand for [State] and [Trigger] types, respectively
 * */
interface StateMachine<S, T> {
    val id: String
    val state: State<S, T>
    val context: StateMachineContext<S, T>

    /** Trigger a state machine to run */
    fun trigger(trigger: Trigger<T>?): State<S, T>
    fun start()
    fun stop()
}
