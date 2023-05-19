package statemachine.configuration.state

import statemachine.state.BaseState
import statemachine.state.State

class DefaultStatesConfiguration<S, T> : StatesConfiguration<S, T> {
    private lateinit var initialState: State<S>
    private val states = mutableSetOf<State<S>>()
    override fun setInitial(state: S) {
        if (this::initialState.isInitialized) {
            states.remove(this.initialState)
        }
        this.initialState = BaseState.initial(state)
        states.add(initialState)
    }

    override fun setTerminal(state: S) {
        BaseState.end(state)
    }

    override fun add(state: S) {
        states.add(BaseState.create(state))
    }



    /**
     * Return an *immutable* State set
     */
    override fun getStates(): Set<State<S>> {
        return states.toSet()
    }
}