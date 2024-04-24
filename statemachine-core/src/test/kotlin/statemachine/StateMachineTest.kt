package statemachine

import org.junit.jupiter.api.Test
import statemachine.action.StateAction
import statemachine.action.TransitionAction.Companion.create
import statemachine.definition.StateMachineDefiner
import statemachine.definition.state.StatesDefiner
import statemachine.definition.transition.TransitionsDefiner
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.Guard
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.StateMachineTestUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StateMachineTest {
    private val stateMachineFactory = DefaultStateMachineFactory<S, T>()

    @Test
    fun testBasicStateMachineFlow() {
        val stateMachineDefiner = StateMachineTestUtil.basicStateMachineDefiner()

        val sm: StateMachine<S, T> = stateMachineFactory.create("TEST_ID", stateMachineDefiner.getDefinition()).also { it.start() }
        assertEquals(S.INITIAL, sm.state.id)

        sm.trigger(createTrigger(T.MOVE_TO_A)).also { assertEquals(sm.state.id, S.STATE_A) }
        sm.trigger(createTrigger(T.MOVE_TO_B)).also { assertEquals(sm.state.id, S.STATE_B) }
        sm.trigger(createTrigger(T.MOVE_TO_C)).also { assertNotEquals(sm.state.id, S.STATE_C) }
        sm.trigger(createTrigger(T.END)).also { assertEquals(sm.state.id, S.TERMINAL_STATE) }
    }

    @Test
    fun testStateMachineChoiceStateFlow() {
        var shouldEnd = false

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
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
                // Transition to STATE_C will be allowed only if shouldEnd is false
                definer.add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, Guard.ofPredicate { !shouldEnd })
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.MOVE_TO_C_OR_END, Guard.ofPredicate { shouldEnd })
                definer.add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        val stateMachineDefinition = stateMachineDefiner.getDefinition()

        var sm: StateMachine<S, T> = stateMachineFactory.createStarted("SHOULD_STOP_AT_STATE_C", stateMachineDefinition)
            .also { assertEquals(S.INITIAL, it.state.id) }

        sm.trigger(createTrigger(T.MOVE_TO_A)).also { assertEquals(S.STATE_A, sm.state.id) }
        sm.trigger(createTrigger(T.MOVE_TO_B)).also { assertEquals(S.STATE_B, sm.state.id) }

        sm.trigger(createTrigger(T.MOVE_TO_C_OR_END)).also { assertEquals(S.STATE_C, sm.state.id) }
        sm = stateMachineFactory.createStarted("SHOULD_END", stateMachineDefinition)
        shouldEnd = true
        sm.apply {
            trigger(createTrigger(T.MOVE_TO_A))
            trigger(createTrigger(T.MOVE_TO_B))
            trigger(createTrigger(T.MOVE_TO_C_OR_END))
        }.also { assertEquals(S.TERMINAL_STATE, sm.state.id) }
    }

    @Test
    fun testStateMachineTriggerlessFlow() {
        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.simple(S.STATE_C)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(S.STATE_A, S.STATE_B, null, positiveGuard)
                definer.add(S.STATE_B, S.TERMINAL_STATE, null, positiveGuard)
            }
        }

        val stateMachineDefinition = stateMachineDefiner.getDefinition()
        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", stateMachineDefinition)
        assertEquals(S.INITIAL, sm.state.id)
        sm.trigger(createTrigger(T.MOVE_TO_A))
        assertEquals(S.TERMINAL_STATE, sm.state.id)
    }

    @Test
    fun testStateMachine_TransitionActions_RunningSequentially() {
        val output = mutableListOf<Int>()

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
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
                definer.add(
                    S.STATE_B,
                    S.STATE_C,
                    T.FORCE_MOVE_TO_C,
                    positiveGuard,
                    listOf(
                        create { output.add(1) },
                        create { output.add(2) },
                        create { output.add(3) },
                    ),
                )
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", stateMachineDefiner.getDefinition())
            .apply {
                trigger(createTrigger(T.MOVE_TO_A))
                trigger(createTrigger(T.MOVE_TO_B))
                trigger(createTrigger(T.FORCE_MOVE_TO_C))
            }
        assertEquals(S.STATE_C, sm.state.id)
        assertEquals(output, listOf(1, 2, 3))
    }

    @Test
    fun testStateMachine_stateActions_RunningSequentially() {
        val output = mutableListOf<Int>()

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A, StateAction.create { output.add(2) }, StateAction.create { output.add(3) })
                definer.simple(S.STATE_B, StateAction.create { output.add(5) }, StateAction.create { output.add(6) })
                definer.terminal(S.TERMINAL_STATE, StateAction.create { output.add(8) })
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, create { output.add(1) })
                definer.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard, create { output.add(4) })
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard, create { output.add(7) })
            }
        }

        stateMachineFactory.createStarted("TEST", stateMachineDefiner.getDefinition())
            .also {
                assertEquals(S.INITIAL, it.state.id)
            }
            .apply {
                trigger(createTrigger(T.MOVE_TO_A))
                trigger(createTrigger(T.MOVE_TO_B))
                trigger(createTrigger(T.END))
            }

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8), output)
    }
}
