package statemachine.configuration

import statemachine.state.State
import statemachine.transition.TransitionSet

class StateMachineConfiguration<S, T> {
    lateinit var initialState: State<S>
    lateinit var states: Set<State<S>>
    lateinit var transitions: TransitionSet<S, T>
}
