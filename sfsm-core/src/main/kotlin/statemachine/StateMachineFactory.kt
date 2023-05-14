package statemachine

import statemachine.configuration.DefaultStateMachineConfiguration

interface StateMachineFactory<S, T> {
    val configuration: DefaultStateMachineConfiguration<S, T>
    fun create(id: String): StateMachine<S, T>
}
