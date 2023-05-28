package statemachine.configuration

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.util.S
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.Ignore

class DefaultStateMachineConfigurationValidationTest {

    @Test
    fun testMultipleInitialStatesThrowsException() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            setInitial(S.STATE_A)
            simple(S.STATE_B)
            setTerminal(S.TERMINAL_STATE)
        }

        assertThrows<StateMachineConfigurationException> {
            config.process()
        }
    }

    @Test
    fun testMissingEndStateThrowsException() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
        }

        assertThrows<StateMachineConfigurationException> {
            config.process()
        }
    }

    @Test
    fun testUnknownStatesInTransitionsThrowsException() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineConfigurationException> {
            config.process()
        }
    }

    @Test
    fun testNonChoiceStateMultipleOutgoingTransitionSameTriggerThrowsException() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            simple(S.STATE_C)
            setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // STATE_B is NOT a choice state, yet treated like one.
            add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_B, S.TERMINAL_STATE, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineConfigurationException> {
            config.process()
        }
    }

    @Test
    fun testChoiceStateSingleOutgoingTransitionThrowsException() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            choice(S.STATE_B)
            simple(S.STATE_C)
            setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // STATE_B is NOT a choice state, yet treated like one.
            add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        assertThrows<StateMachineConfigurationException> {
            config.process()
        }
    }
}
