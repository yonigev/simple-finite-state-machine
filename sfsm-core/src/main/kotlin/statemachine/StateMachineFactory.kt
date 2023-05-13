package statemachine

import statemachine.configuration.StateMachineConfiguration

interface StateMachineFactory<S, T> {
    val configuration: StateMachineConfiguration<S, T>
    fun create(id: String): StateMachine<S, T>
}
