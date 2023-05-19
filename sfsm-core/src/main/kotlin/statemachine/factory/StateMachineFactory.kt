package statemachine.factory

import statemachine.StateMachine
import statemachine.configuration.StateMachineConfiguration

/**
 * A factory that creates [StateMachine] instances based on a [StateMachineConfiguration]
 */
interface StateMachineFactory<S, T> {
    val configuration: StateMachineConfiguration<S, T>

    fun create(id: String): StateMachine<S, T>
}
