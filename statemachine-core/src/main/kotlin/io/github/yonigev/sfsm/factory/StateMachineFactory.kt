package io.github.yonigev.sfsm.factory

import io.github.yonigev.sfsm.StateMachine
import io.github.yonigev.sfsm.definition.StateMachineDefinition

/**
 * A factory that creates [StateMachine] instances based on a [StateMachineDefinition]
 */
interface StateMachineFactory<S, T> {
    fun create(machineId: String, definition: StateMachineDefinition<S, T>): StateMachine<S, T>
}
