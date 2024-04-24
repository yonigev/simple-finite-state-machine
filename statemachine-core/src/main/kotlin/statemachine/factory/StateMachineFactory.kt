package statemachine.factory

import statemachine.StateMachine
import statemachine.definition.StateMachineDefinition

/**
 * A factory that creates [StateMachine] instances based on a [StateMachineDefinition]
 */
interface StateMachineFactory<S, T> {
    fun create(machineId: String, definition: StateMachineDefinition<S, T>): StateMachine<S, T>
}
