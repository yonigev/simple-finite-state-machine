package statemachine.configuration.state

interface StatesConfiguration<S, T> {
    /**
     * Defines an initial state for the state machine.
     * A StateMachine can only have one initial state.
     */
    fun initial(initialState: S)

    /**
     * Adds a possible state of the StateMachine
     */
    fun addState(state: S);


}