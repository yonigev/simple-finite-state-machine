package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.guard.Guard
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.transition.TransitionContext
import io.github.yonigev.sfsm.uml.definers.DummyStateMachineDefiner.S
import io.github.yonigev.sfsm.uml.definers.DummyStateMachineDefiner.T
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals

const val TEST_MACHINE_NAME = "TEST"

// A basic state machine definition
fun createDummyStateMachineDefinition(name: String): StateMachineDefinition<S, T> {
    val states = setOf<State<S, T>>(
        State.initial(S.START),
        State.create(S.S1),
        State.create(S.S2),
        State.choice(S.S3),
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

// A basic state machine definition that also has state entry and exit actions
fun createDummyStateMachineDefinitionWithActions(name: String = "TEST"): StateMachineDefinition<S, T> {
    val states = setOf(
        State.initial(S.START),
        State.create(S.S1, entryAction = MyEntryAction(), exitAction = MyExitAction()),
        State.create(S.S2),
        State.choice(S.S3, entryAction = MyEntryAction(), exitAction = MyExitAction()),
        State.terminal(S.END1),
        State.terminal(S.END2),
    )

    val transitions = setOf(
        Transition.create(S.START, S.S1, T.GOTO_S1),
        Transition.create(S.S1, S.S2, T.GOTO_S2, guard = MyGuard()),
        Transition.create(S.S2, S.S3, T.GOTO_S3),
        Transition.create(S.S3, S.END1, T.GOTO_END1, transitionActions = listOf(MyTransitionAction(), MyTransitionAction())),
        Transition.create(S.S3, S.END2, T.GOTO_END2)
    )

    return StateMachineDefinition(name, states, transitions)
}

fun assertUmlEquals(expectedUml: String, actualUmlFile: File) {
    val actualUml = actualUmlFile.readText()
    assertUmlEquals(expectedUml, actualUml)
}

fun assertUmlEquals(expectedUml: String, actualUml: String) {
    val actual = actualUml.replace("\\s".toRegex(), "")
    val expected = expectedUml.replace("\\s".toRegex(), "")
    assertEquals(expected, actual)
}

fun assertUmlContains(contained: String, contains: String) {
    assertContains(contains.replace("\\s".toRegex(), ""), contained.replace("\\s".toRegex(), ""))
}

class MyEntryAction: StateAction<S, T> {
    override fun act(stateMachineContext: StateMachineContext<S, T>) {}
}

class MyExitAction: StateAction<S, T> {
    override fun act(stateMachineContext: StateMachineContext<S, T>) {}
}

class MyGuard: Guard<S, T> {
    override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
        return true
    }
}

class MyTransitionAction: TransitionAction<S, T> {
    override fun act(transitionContext: TransitionContext<S, T>) {
    }
}