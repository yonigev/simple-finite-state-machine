package statemachine.configuration.state

import statemachine.action.StateAction
import statemachine.state.State

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
     * Adds a simple state of the state machine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun simple(stateId: S, entryAction: StateAction<S, T>? = null, exitAction: StateAction<S, T>? = null)

    /**
     * Adds a simple state of the state machine
     */
    fun simple(stateId: S)

    /**
     * Adds a simple state of the state machine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun choice(stateId: S, entryAction: StateAction<S, T>? = null, exitAction: StateAction<S, T>? = null)

    /**
     * Adds a choice state of the state machine.
     * Note that this state should have at least two outgoing transitions with the same trigger.
     */
    fun choice(stateId: S)

    /**
     * Defines a terminal state for the state machine, with an optional entry action
     * A StateMachine can have multiple end states
     */
    fun terminal(stateId: S, entryAction: StateAction<S, T>? = null)

    /**
     * Defines a terminal state for the state machine.
     * A StateMachine can have multiple end states
     */
    fun terminal(stateId: S)

    /**
     * Returns the configured [State]s definition
     * used by the [statemachine.configuration.StateMachineConfiguration] to configure the StateMachine
     */
    fun getStates(): Set<State<S, T>>
}
