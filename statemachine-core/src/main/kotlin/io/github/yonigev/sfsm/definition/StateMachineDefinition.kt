package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition

open class StateMachineDefinition<S, T>(
    val name: String,
    val states: Set<State<S, T>>,
    val transitions: Set<Transition<S, T>>,
)
