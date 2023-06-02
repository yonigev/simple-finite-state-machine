package statemachine.configuration.state

import statemachine.state.State

/**
 * Used to configure [statemachine.StateMachine] states
 */
interface StatesConfiguration<S, T> {
    /**
     * Defines an initial state for the state machine.
     * A StateMachine can only have one initial state.
     */
    fun setInitial(state: S)

    /**
     * Adds a simple state of the StateMachine
     */
    fun simple(state: S)

    /**
     * Adds a choice state of the StateMachine.
     * Note that this state should have at least two outgoing transitions with the same trigger.
     */
    fun choice(state: S)

    /**
     * Defines a terminal state for the state machine.
     * A StateMachine can have multiple end states
     */
    fun setTerminal(state: S)

    /**
     * Returns the configured [State] definitions
     * used by the [statemachine.configuration.StateMachineConfiguration] to configure the StateMachine
     */
    fun getStates(): Set<State<S>>
}
