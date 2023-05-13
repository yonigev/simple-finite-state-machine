package statemachine

import statemachine.configuration.StateMachineConfiguration

/** A Default StateMachineFactory */
class DefaultStateMachineFactory<S, T>(
    override val configuration: StateMachineConfiguration<S, T>,
) : StateMachineFactory<S, T> {
    override fun create(id: String): StateMachine<S, T> {
        return DefaultStateMachine(
            id,
            configuration.initialState,
            configuration.states,
            configuration.transitions,
        )
    }
}
