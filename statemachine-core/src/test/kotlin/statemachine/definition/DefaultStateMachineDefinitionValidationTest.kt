package statemachine.definition

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.util.S
import statemachine.util.T
import statemachine.util.positiveGuard

class DefaultStateMachineDefinitionValidationTest {

    @Test
    fun testMultipleInitialStatesThrowsException() {
        val definition = DefaultStateMachineDefinition<S, T>()
        definition.defineStates().apply {
            setInitial(S.INITIAL)
            setInitial(S.STATE_A)
            simple(S.STATE_B)
            terminal(S.TERMINAL_STATE)
        }

        assertThrows<StateMachineDefinitionException> {
            definition.process()
        }
    }

    @Test
    fun testMissingEndStateThrowsException() {
        val definition = DefaultStateMachineDefinition<S, T>()
        definition.defineStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
        }

        assertThrows<StateMachineDefinitionException> {
            definition.process()
        }
    }

    @Test
    fun testUnknownStatesInTransitionsThrowsException() {
        val definition = DefaultStateMachineDefinition<S, T>()
        definition.defineStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            terminal(S.TERMINAL_STATE)
        }

        definition.defineTransitions().apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineDefinitionException> {
            definition.process()
        }
    }

    @Test
    fun testNonChoiceStateMultipleOutgoingTransitionSameTriggerThrowsException() {
        val definition = DefaultStateMachineDefinition<S, T>()
        definition.defineStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            simple(S.STATE_C)
            terminal(S.TERMINAL_STATE)
        }

        definition.defineTransitions().apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // STATE_B is NOT a choice state, yet treated like one.
            add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_B, S.TERMINAL_STATE, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineDefinitionException> {
            definition.process()
        }
    }

    @Test
    fun testChoiceStateSingleOutgoingTransitionThrowsException() {
        val definition = DefaultStateMachineDefinition<S, T>()
        definition.defineStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            choice(S.STATE_B)
            simple(S.STATE_C)
            terminal(S.TERMINAL_STATE)
        }

        definition.defineTransitions().apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // STATE_B is NOT a choice state, yet treated like one.
            add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineDefinitionException> {
            definition.process()
        }
    }
}
