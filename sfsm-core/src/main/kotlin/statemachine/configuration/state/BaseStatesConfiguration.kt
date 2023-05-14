package statemachine.configuration.state

import statemachine.state.BaseState
import statemachine.state.State

class BaseStatesConfiguration<S, T> : StatesConfiguration<S, T> {
    private var initial: State<S>? = null
    private val states = mutableSetOf<State<S>>()
    override fun initial(initialState: S) {
        this.initial = BaseState(initialState)
        addState(initialState)
    }

    override fun addState(state: S) {
        states.add(BaseState(state))
    }


}