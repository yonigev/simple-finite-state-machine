package statemachine.configuration.state

import statemachine.state.State

/**
 * A default implementation for the [DefaultStatesConfiguration]
 */
class DefaultStatesConfiguration<S, T> : StatesConfiguration<S, T> {
    private val states = mutableSetOf<State<S>>()
    override fun setInitial(state: S) {
        states.add(State.initial(state))
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
     * Returns an *immutable* State set
     */
    override fun getStates(): Set<State<S>> {
        return states.toSet()
    }
}
