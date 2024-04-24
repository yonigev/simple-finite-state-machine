package statemachine.factory

import statemachine.StateMachine
import statemachine.definition.StateMachineDefiner
import statemachine.definition.StateMachineDefinition

/**
 * A factory that creates [StateMachine] instances based on a [StateMachineDefinition]
 */
interface StateMachineFactory<S, T> {
    fun create(machineId: String, definer: StateMachineDefiner<S, T>): StateMachine<S, T>
}
