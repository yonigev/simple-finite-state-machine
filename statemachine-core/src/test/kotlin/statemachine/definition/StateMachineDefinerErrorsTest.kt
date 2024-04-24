package statemachine.definition

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.definition.state.StatesDefiner
import statemachine.definition.transition.TransitionsDefiner
import statemachine.util.S
import statemachine.util.T
import statemachine.util.positiveGuard

class StateMachineDefinerErrorsTest {

    @Test
    fun testMultipleInitialStatesThrowsException() {
        val definer = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.setInitial(S.STATE_A)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
            }
        }

        assertThrows<StateMachineDefinitionException> {
            definer.getDefinition()
        }
    }

    @Test
    fun testMissingEndStateThrowsException() {
        val definer = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {}
        }

        assertThrows<StateMachineDefinitionException> {
            definer.getDefinition()
        }
    }

    @Test
    fun testUnknownStatesInTransitionsThrowsException() {
        val definer = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(S.STATE_A, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        assertThrows<StateMachineDefinitionException> {
            definer.getDefinition()
        }
    }

    @Test
    fun testNonChoiceStateMultipleOutgoingTransitionSameTriggerThrowsException() {
        val definer = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.simple(S.STATE_C)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                // STATE_B is NOT a choice state, yet treated like one.
                definer.add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.MOVE_TO_C_OR_END, positiveGuard)
                definer.add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        assertThrows<StateMachineDefinitionException> {
            definer.getDefinition()
        }
    }

    @Test
    fun testChoiceStateSingleOutgoingTransitionThrowsException() {
        val definer = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.choice(S.STATE_B)
                definer.simple(S.STATE_C)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                // STATE_B is NOT a choice state, yet treated like one.
                definer.add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, positiveGuard)
                definer.add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        assertThrows<StateMachineDefinitionException> {
            definer.getDefinition()
        }
    }
}
