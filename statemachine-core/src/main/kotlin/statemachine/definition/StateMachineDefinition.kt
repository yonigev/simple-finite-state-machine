package statemachine.definition

import statemachine.state.State
import statemachine.transition.Transition

open class StateMachineDefinition<S, T>(
    val name: String? = null,
    val states: Set<State<S, T>>,
    val transitions: Set<Transition<S, T>>,
)
