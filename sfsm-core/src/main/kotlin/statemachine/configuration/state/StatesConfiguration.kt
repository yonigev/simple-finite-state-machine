package statemachine.configuration.state

import statemachine.state.State

interface StatesConfiguration<S, T> {
    /**
     * Defines an initial state for the state machine.
     * A StateMachine can only have one initial state.
     */
    fun setInitial(state: S)

    /**
     * Adds a possible state of the StateMachine
     */
    fun add(state: S)

    /**
     * Defines a terminal state for the state machine.
     * A StateMachine can have multiple end states
     */
    fun setTerminal(state: S)

    fun getStates(): Set<State<S>>
}
