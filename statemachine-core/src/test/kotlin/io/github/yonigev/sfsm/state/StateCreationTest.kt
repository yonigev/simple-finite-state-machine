package io.github.yonigev.sfsm.state

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StateCreationTest {
    @Test
    fun testInitialStateCreation() {
        val state = State.initial<S, T>(S.STATE_A)
        assertEquals(state.id, S.STATE_A)
        assertEquals(State.PseudoStateType.INITIAL, state.type)
        assertEquals(state.entryAction, null)
        assertEquals(state.exitAction, null)
    }

    @Test
    fun testSimpleStateCreation() {
        val someAction = StateAction.create<S, T> {}
        val state = State.create(S.STATE_A, entryAction = someAction, exitAction = someAction)
        assertEquals(state.id, S.STATE_A)
        assertEquals(State.PseudoStateType.SIMPLE, state.type)
        assertEquals(state.entryAction, someAction)
        assertEquals(state.exitAction, someAction)
        assertEquals(state.toString(), "STATE_A")
    }

    @Test
    fun testChoiceStateCreation() {
        val state = State.choice<S, T>(S.STATE_A)
        assertEquals(state.id, S.STATE_A)
        assertEquals(State.PseudoStateType.CHOICE, state.type)
        assertEquals(state.entryAction, null)
        assertEquals(state.exitAction, null)
    }

    @Test
    fun testTerminalStateCreation() {
        val someAction = StateAction.create<S, T> {}
        val state = State.terminal(S.STATE_A, entryAction = someAction)
        assertEquals(state.id, S.STATE_A)
        assertEquals(State.PseudoStateType.TERMINAL, state.type)
        assertEquals(state.entryAction, someAction)
        assertEquals(state.exitAction, null)
    }
}
