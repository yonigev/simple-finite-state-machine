package statemachine.configuration

import statemachine.state.State
import statemachine.transition.Transition

interface StateMachineConfiguration<S, T> {
    fun initial(initialState: State<S>): StateMachineConfiguration<S, T>
    fun states(vararg states: State<S>): StateMachineConfiguration<S, T>
    fun transitions(vararg transitions: Transition<S, T>): StateMachineConfiguration<S, T>
}