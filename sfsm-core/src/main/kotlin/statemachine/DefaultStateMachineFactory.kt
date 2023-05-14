package statemachine

import statemachine.configuration.DefaultStateMachineConfiguration

/** A Default StateMachineFactory */
class DefaultStateMachineFactory<S, T>(
    override val configuration: DefaultStateMachineConfiguration<S, T>,
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
