package statemachine.configuration.state

import statemachine.action.StateAction
import statemachine.state.State

/**
 * A definition of a [State] which includes the [State] itself and its entry and exit actions
 */
class StateDefinition<S, T>(
    val state: State<S>,
    val entryAction: StateAction<S, T>?,
    val exitAction: StateAction<S, T>?,
)
