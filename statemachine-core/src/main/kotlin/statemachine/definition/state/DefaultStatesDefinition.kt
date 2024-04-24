package statemachine.definition.state

import statemachine.action.StateAction
import statemachine.state.State

/**
 * A default implementation for the [StatesDefinition]
 */
class DefaultStatesDefinition<S, T> : StatesDefinition<S, T> {
    private val states = mutableListOf<State<S, T>>()
    override fun setInitial(stateId: S) {
        addState(State.initial(stateId))
    }

    override fun terminal(stateId: S, entryAction: StateAction<S, T>?) {
        addState(State.terminal(stateId, entryAction))
    }

    override fun terminal(stateId: S) {
        addState(State.terminal(stateId))
    }

    override fun simple(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addState(State.create(stateId, State.PseudoStateType.SIMPLE, entryAction, exitAction))
    }

    override fun simple(stateId: S) {
        simple(stateId, null, null)
    }

    override fun choice(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addState(State.choice(stateId, entryAction, exitAction))
    }

    override fun choice(stateId: S) {
        choice(stateId, null, null)
    }

    private fun addState(
        state: State<S, T>,
    ) {
        states.add(state)
    }

    /**
     * Returns an *immutable* State set
     */
    override fun getStates(): Set<State<S, T>> {
        return states.toSet()
    }
}
