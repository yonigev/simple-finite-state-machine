package statemachine.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.StateMachine
import statemachine.action.createAction
import statemachine.configuration.StateMachineConfigurationException
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.Guard.Companion.createGuard
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition
import statemachine.util.S
import statemachine.util.StateMachineUtil
import statemachine.util.StateMachineUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals

class StateMachineExceptionHandlingTest {
    @Test
    fun testExceptionThrownInGuardIsPropagated() {
        val config = StateMachineUtil.createConfig()
        val exceptionThrowingGuard = createGuard<S, T> { throw Exception("Test exception") }
        (config.configureTransitions() as (DefaultTransitionsConfiguration))
            .add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, exceptionThrowingGuard)
        val factory = DefaultStateMachineFactory(config)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testExceptionThrownInActionIsPropagated() {
        val exceptionThrowingAction =
            createAction<S, T> { throw Exception("Test Exception thrown in a Transition Action") }
        val config = StateMachineUtil.createConfig()
        val transition: Transition<S, T> =
            DefaultTransition(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, listOf(exceptionThrowingAction))
        config.configureTransitions().add(transition)
        val factory = DefaultStateMachineFactory(config)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStoppedAndTriggered() {
        val config = StateMachineUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.create("TEST_ID")
        assertEquals(S.INITIAL, sm.state.getId())
        assertThrows<StateMachineConfigurationException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStartedInTerminalState() {
        val config = StateMachineUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
        sm.trigger(createTrigger(T.MOVE_TO_A))
        sm.trigger(createTrigger(T.MOVE_TO_B))
        sm.trigger(createTrigger(T.END))
        assertEquals(S.TERMINAL_STATE, sm.state.getId())
        assertThrows<StateMachineConfigurationException> { sm.start() }
    }
}
