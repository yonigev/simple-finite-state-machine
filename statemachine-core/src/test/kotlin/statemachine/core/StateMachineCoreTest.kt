package statemachine.core

import org.junit.jupiter.api.Test
import statemachine.StateMachine
import statemachine.action.createAction
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.factory.DefaultStateMachineFactory
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition
import statemachine.util.S
import statemachine.util.StateMachineUtil
import statemachine.util.StateMachineUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StateMachineCoreTest {
    @Test
    fun testStateMachineBuilding() {
        val config = StateMachineUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        sm.stop()
    }

    @Test
    fun testBasicStateMachineFlow() {
        val config = StateMachineUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        assertEquals(S.INITIAL, sm.state.getId())

        sm.trigger(createTrigger(T.MOVE_TO_A))
        assertEquals(sm.state.getId(), S.STATE_A)

        sm.trigger(createTrigger(T.MOVE_TO_B))
        assertEquals(sm.state.getId(), S.STATE_B)

        sm.trigger(createTrigger(T.MOVE_TO_C))
        assertNotEquals(sm.state.getId(), S.STATE_C)

        sm.trigger(createTrigger(T.END))
        assertEquals(sm.state.getId(), S.TERMINAL_STATE)
    }

    @Test
    fun testStateMachineFlow_Triggerless() {
        val config = StateMachineUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)

        (config.configureTransitions() as DefaultTransitionsConfiguration).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
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
                createAction { output.add(1) },
                createAction { output.add(2) },
                createAction { output.add(3) },
            ),
        )
        val config = StateMachineUtil.createConfig()
            .also { it.configureTransitions().add(transition) }
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")

        sm.trigger(createTrigger(T.MOVE_TO_A))
        sm.trigger(createTrigger(T.MOVE_TO_B))
        sm.trigger(createTrigger(T.FORCE_MOVE_TO_C))
        assertEquals(S.STATE_C, sm.state.getId())
        assertEquals(output, listOf(1, 2, 3))
    }
}
