package io.github.yonigev.sfsm.definition.state

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.state.State

/**
 * Used to create and define [io.github.yonigev.sfsm.StateMachine] states
 */
class StatesDefiner<S, T> {
    private val states = mutableListOf<State<S, T>>()

    /**
     * Defines an initial state for the state machine.
     * A StateMachine can only have one initial state.
     */
    fun setInitial(stateId: S) {
        addState(State.initial(stateId))
    }

    /**
     * Adds a simple state of the state machine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun simple(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addState(State.create(stateId, State.PseudoStateType.SIMPLE, entryAction, exitAction))
    }

    /**
     * Adds a simple state of the state machine
     */
    fun simple(stateId: S) {
        simple(stateId, null, null)
    }

    /**
     * Adds a choice state of the state machine, with optional entry and exit actions
     * Added separately to allow Java callers to use the shorter method.
     */
    fun choice(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addState(State.choice(stateId, entryAction, exitAction))
    }

    /**
     * Adds a choice state of the state machine.
     * Note that this state should have at least two outgoing transitions with the same trigger.
     */
    fun choice(stateId: S) {
        choice(stateId, null, null)
    }

    /**
     * Defines a terminal state for the state machine, with an optional entry action
     * A StateMachine can have multiple end states
     */
    fun terminal(stateId: S, entryAction: StateAction<S, T>?) {
        addState(State.terminal(stateId, entryAction))
    }

    /**
     * Defines a terminal state for the state machine.
     * A StateMachine can have multiple end states
     */
    fun terminal(stateId: S) {
        addState(State.terminal(stateId))
    }

    private fun addState(
        state: State<S, T>,
    ) {
        states.add(state)
    }

    /**
     * Returns an *immutable* State set
     */
    fun getStates(): Set<State<S, T>> {
        return states.toSet()
    }
}
