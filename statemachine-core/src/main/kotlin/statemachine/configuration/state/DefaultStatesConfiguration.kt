package statemachine.configuration.state

import statemachine.action.StateAction
import statemachine.state.State

/**
 * A default implementation for the [DefaultStatesConfiguration]
 */
class DefaultStatesConfiguration<S, T> : StatesConfiguration<S, T> {
    private val statesDefinitions = mutableListOf<StateDefinition<S, T>>()
    override fun setInitial(stateId: S) {
        addStateDefinition(State.initial(stateId))
    }

    override fun terminal(stateId: S, entryAction: StateAction<S, T>?) {
        addStateDefinition(State.terminal(stateId), entryAction, null)
    }

    override fun terminal(stateId: S) {
        addStateDefinition(State.terminal(stateId), null, null)
    }

    override fun simple(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addStateDefinition(State.create(stateId), entryAction, exitAction)
    }

    override fun simple(stateId: S) {
        simple(stateId, null, null)
    }

    override fun choice(stateId: S, entryAction: StateAction<S, T>?, exitAction: StateAction<S, T>?) {
        addStateDefinition(State.choice(stateId), entryAction, exitAction)
    }

    override fun choice(stateId: S) {
        choice(stateId, null, null)
    }

    private fun addStateDefinition(
        state: State<S>,
        entryAction: StateAction<S, T>? = null,
        exitAction: StateAction<S, T>? = null,
    ) {
        statesDefinitions.add(StateDefinition(state, entryAction, exitAction))
    }

    /**
     * Returns an *immutable* State set
     */
    override fun getStateDefinitions(): Set<StateDefinition<S, T>> {
        return statesDefinitions.toSet()
    }
}
