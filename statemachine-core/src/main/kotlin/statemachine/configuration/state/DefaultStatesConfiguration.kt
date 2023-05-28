package statemachine.configuration.state

import statemachine.state.State

class DefaultStatesConfiguration<S, T> : StatesConfiguration<S, T> {
    private lateinit var initialState: State<S>
    private val states = mutableSetOf<State<S>>()
    override fun setInitial(state: S) {
        this.initialState = State.initial(state)
        states.add(initialState)
    }

    override fun setTerminal(state: S) {
        State.end(state).also { states.add(it) }
    }

    override fun simple(state: S) {
        states.add(State.create(state))
    }

    override fun choice(state: S) {
        states.add(State.create(state, State.PseudoStateType.CHOICE))
    }

    /**
     * Return an *immutable* State set
     */
    override fun getStates(): Set<State<S>> {
        return states.toSet()
    }
}
