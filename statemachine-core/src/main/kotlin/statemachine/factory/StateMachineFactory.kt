package statemachine.factory

import statemachine.StateMachine
import statemachine.definition.StateMachineDefinition

/**
 * A factory that creates [StateMachine] instances based on a [StateMachineDefinition]
 */
interface StateMachineFactory<S, T> {
    val definition: StateMachineDefinition<S, T>

    fun create(id: String): StateMachine<S, T>
}
