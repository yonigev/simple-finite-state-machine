package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.uml.DummyStateMachineDefiner.S
import io.github.yonigev.sfsm.uml.DummyStateMachineDefiner.T

const val TEST_MACHINE_NAME = "TEST"
fun createDummyStateMachineDefinition(name: String): StateMachineDefinition<S, T> {
    val states = setOf<State<S, T>>(
        State.initial(S.START),
        State.create(S.S1),
        State.create(S.S2),
        State.create(S.S3),
        State.terminal(S.END1),
        State.terminal(S.END2),
    )

    val transitions = setOf(
        Transition.create(S.START, S.S1, T.GOTO_S1),
        Transition.create(S.S1, S.S2, T.GOTO_S2),
        Transition.create(S.S2, S.S3, T.GOTO_S3),
        Transition.create(S.S3, S.END1, T.GOTO_END1),
        Transition.create(S.S3, S.END2, T.GOTO_END2)
    )

    return StateMachineDefinition(name, states, transitions)
}