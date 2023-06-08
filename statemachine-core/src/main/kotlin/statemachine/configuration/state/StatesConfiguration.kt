package statemachine.configuration.state

import statemachine.action.StateAction

/**
 * Used to configure [statemachine.StateMachine] states
 */
interface StatesConfiguration<S, T> {
    /**
     * Defines an initial state for the state machine.
     * A StateMachine can only have one initial state.
     */
    fun setInitial(stateId: S)

    /**
     * Adds a simple state of the StateMachine
     */
    fun simple(stateId: S)

    /**
     * Adds a simple state of the StateMachine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun simple(stateId: S, entryAction: StateAction<S, T>? = null, exitAction: StateAction<S, T>? = null)

    /**
     * Adds a choice state of the StateMachine.
     * Note that this state should have at least two outgoing transitions with the same trigger.
     */
    fun choice(stateId: S)

    /**
     * Adds a simple state of the StateMachine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun choice(stateId: S, entryAction: StateAction<S, T>? = null, exitAction: StateAction<S, T>? = null)

    /**
     * Defines a terminal state for the state machine.
     * A StateMachine can have multiple end states
     */
    fun setTerminal(stateId: S)

    /**
     * Returns the configured [StateDefinition] definitions
     * used by the [statemachine.configuration.StateMachineConfiguration] to configure the StateMachine
     */
    fun getStateDefinitions(): List<StateDefinition<S, T>>
}
