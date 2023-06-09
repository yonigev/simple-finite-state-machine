package statemachine

import org.junit.jupiter.api.Test
import statemachine.action.StateAction
import statemachine.action.TransitionAction.Companion.create
import statemachine.configuration.DefaultStateMachineConfiguration
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.Guard
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.StateMachineTestUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StateMachineTest {

    @Test
    fun testBasicStateMachineFlow() {
        val config = StateMachineTestUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        assertEquals(S.INITIAL, sm.state.getId())

        sm.trigger(createTrigger(T.MOVE_TO_A)).also { assertEquals(sm.state.getId(), S.STATE_A) }
        sm.trigger(createTrigger(T.MOVE_TO_B)).also { assertEquals(sm.state.getId(), S.STATE_B) }
        sm.trigger(createTrigger(T.MOVE_TO_C)).also { assertNotEquals(sm.state.getId(), S.STATE_C) }
        sm.trigger(createTrigger(T.END)).also { assertEquals(sm.state.getId(), S.TERMINAL_STATE) }
    }

    @Test
    fun testStateMachineChoiceStateFlow() {
        val config = DefaultStateMachineConfiguration<S, T>()
        var shouldEnd = false
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            choice(S.STATE_B)
            simple(S.STATE_C)
            terminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // Transition to STATE_C will be allowed only if shouldEnd is false
            add(S.STATE_B, S.STATE_C, T.MOVE_TO_C_OR_END, Guard.ofPredicate { !shouldEnd })
            add(S.STATE_B, S.TERMINAL_STATE, T.MOVE_TO_C_OR_END, Guard.ofPredicate { shouldEnd })
            add(S.STATE_C, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        val factory = DefaultStateMachineFactory(config)

        var sm: StateMachine<S, T> = factory.createStarted("SHOULD_STOP_AT_STATE_C")
            .also { assertEquals(S.INITIAL, it.state.getId()) }

        sm.trigger(createTrigger(T.MOVE_TO_A)).also { assertEquals(S.STATE_A, sm.state.getId()) }
        sm.trigger(createTrigger(T.MOVE_TO_B)).also { assertEquals(S.STATE_B, sm.state.getId()) }

        sm.trigger(createTrigger(T.MOVE_TO_C_OR_END)).also { assertEquals(S.STATE_C, sm.state.getId()) }
        sm = factory.createStarted("SHOULD_END")
        shouldEnd = true
        sm.apply {
            trigger(createTrigger(T.MOVE_TO_A))
            trigger(createTrigger(T.MOVE_TO_B))
            trigger(createTrigger(T.MOVE_TO_C_OR_END))
        }.also { assertEquals(S.TERMINAL_STATE, sm.state.getId()) }
    }

    @Test
    fun testStateMachineTriggerlessFlow() {
        val config = StateMachineTestUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)

        (config.configureTransitions() as DefaultTransitionsConfiguration).apply {
            add(S.STATE_A, S.STATE_B, null, positiveGuard)
            add(S.STATE_B, S.TERMINAL_STATE, null, positiveGuard)
        }
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
        assertEquals(S.INITIAL, sm.state.getId())
        sm.trigger(createTrigger(T.MOVE_TO_A))
        assertEquals(S.TERMINAL_STATE, sm.state.getId())
    }

    @Test
    fun testStateMachine_TransitionActions_RunningSequentially() {
        val output = mutableListOf<Int>()

        val transition: Transition<S, T> = DefaultTransition(
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
        val config = StateMachineTestUtil.createConfig()
            .also { it.configureTransitions().add(transition) }
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
            .apply {
                trigger(createTrigger(T.MOVE_TO_A))
                trigger(createTrigger(T.MOVE_TO_B))
                trigger(createTrigger(T.FORCE_MOVE_TO_C))
            }
        assertEquals(S.STATE_C, sm.state.getId())
        assertEquals(output, listOf(1, 2, 3))
    }

    @Test
    fun testStateMachine_stateActions_RunningSequentially() {
        val output = mutableListOf<Int>()

        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A, StateAction.create { output.add(2) }, StateAction.create { output.add(3) })
            simple(S.STATE_B, StateAction.create { output.add(5) }, StateAction.create { output.add(6) })
            terminal(S.TERMINAL_STATE, StateAction.create { output.add(8) })
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, create { output.add(1) })
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard, create { output.add(4) })
            add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard, create { output.add(7) })
        }

        val factory = DefaultStateMachineFactory(config)

        factory.createStarted("TEST")
            .also { assertEquals(S.INITIAL, it.state.getId()) }
            .apply {
                trigger(createTrigger(T.MOVE_TO_A))
                trigger(createTrigger(T.MOVE_TO_B))
                trigger(createTrigger(T.END))
            }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8), output)
    }
}
