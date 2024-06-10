package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.StateMachineTestUtil
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StateMachineDefinitionValidatorTest {
    val validator = DefinitionValidator<S, T>()

    @Test
    fun test_valid() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        assertTrue(validator.validateStates(definition.states))
        assertTrue(validator.validateTransitions(definition.states, definition.transitions))
    }

    @Test
    fun test_invalidInitialStates() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        val badStates = (listOf(State.initial<S, T>(S.INITIAL)) + definition.states).toSet()

        val badDefinition = StateMachineDefinition("TEST", badStates, definition.transitions)

        val exception = assertThrows<StateMachineDefinitionException> {
            validator.validateStates(badDefinition.states)
        }

        assertNotNull(exception)
        assertFalse(exception.message.isNullOrBlank())
        assertContains(exception.message!!, State.PseudoStateType.INITIAL.toString())
    }

    @Test
    fun test_invalidEndStates() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        val badStates = definition.states.filter { it.type != State.PseudoStateType.TERMINAL }.toSet()

        val badDefinition = StateMachineDefinition("TEST", badStates, definition.transitions)

        val exception = assertThrows<StateMachineDefinitionException> {
            validator.validateStates(badDefinition.states)
        }

        assertNotNull(exception)
        assertFalse(exception.message.isNullOrBlank())
        assertContains(exception.message!!, "END")
    }

    @Test
    fun test_missingStates() {
        val badDefinition = StateMachineDefinition(
            "TEST",
            setOf(State.initial(S.INITIAL), State.terminal(S.TERMINAL_STATE)),
            setOf(
                Transition.create(S.INITIAL, S.STATE_A, T.MOVE_TO_A),
                Transition.create(S.STATE_A, S.TERMINAL_STATE, T.END),
            ),
        )

        assertThrows<StateMachineDefinitionException> {
            validator.validateTransitions(badDefinition.states, badDefinition.transitions)
        }
    }

    @Test
    fun test_choiceState_onlyOneTransition() {
        val badDefinition = StateMachineDefinition(
            "TEST",
            setOf(
                State.initial(S.INITIAL),
                State.choice(S.STATE_A),
                State.terminal(S.TERMINAL_STATE),
            ),
            setOf(
                Transition.create(S.INITIAL, S.STATE_A, T.MOVE_TO_A),
                Transition.create(S.STATE_A, S.TERMINAL_STATE, T.END),
            ),
        )

        val exception = assertThrows<StateMachineDefinitionException> {
            validator.validateTransitions(badDefinition.states, badDefinition.transitions)
        }

        assertContains(exception.message!!.lowercase(), "choice")
    }

    @Test
    fun test_mutipleSimlarTransition_sameSourceState_non_choice() {
        val badDefinition = StateMachineDefinition(
            "TEST",
            setOf(
                State.initial(S.INITIAL),
                State.create(S.STATE_A),
                State.create(S.STATE_B),
                State.create(S.STATE_C),
                State.terminal(S.TERMINAL_STATE),
            ),
            setOf(
                Transition.create(S.INITIAL, S.STATE_A, T.MOVE_TO_A),
                Transition.create(S.STATE_A, S.STATE_B, T.END),
                Transition.create(S.STATE_A, S.STATE_C, T.END),
                Transition.create(S.STATE_A, S.TERMINAL_STATE, T.END),
            ),
        )

        val exception = assertThrows<StateMachineDefinitionException> {
            validator.validateTransitions(badDefinition.states, badDefinition.transitions)
        }

        assertContains(exception.message!!.lowercase(), "choice")
    }
}
