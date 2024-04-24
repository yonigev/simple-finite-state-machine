package statemachine.definition

import statemachine.state.State
import statemachine.transition.Transition

class StateMachineDefinition<S, T>(
    val states: Set<State<S, T>>,
    val transitions: Set<Transition<S, T>>,
)
