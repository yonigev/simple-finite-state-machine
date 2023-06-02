package statemachine.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.StateMachine
import statemachine.StateMachineException
import statemachine.action.Action.Companion.createAction
import statemachine.configuration.DefaultStateMachineConfiguration
import statemachine.configuration.StateMachineConfigurationException
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.Guard.Companion.ofPredicate
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.StateMachineTestUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals

class StateMachineExceptionHandlingTest {
    @Test
    fun testExceptionThrownInGuardIsPropagated() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, ofPredicate { throw Exception("Exception Throwing Guard") })
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
        }
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testExceptionThrownInActionIsPropagated() {
        val config = DefaultStateMachineConfiguration<S, T>()
        config.configureStates().apply {
            setInitial(S.INITIAL)
            simple(S.STATE_A)
            simple(S.STATE_B)
            setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
            add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, createAction<S, T> { throw Exception("Exception thrown in a Transition Action") })
            add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
        }
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStoppedAndTriggered() {
        val config = StateMachineTestUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.create("TEST_ID")
        assertEquals(S.INITIAL, sm.state.getId())
        assertThrows<StateMachineConfigurationException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStartedInTerminalState() {
        val config = StateMachineTestUtil.createConfig()
        val factory = DefaultStateMachineFactory(config)
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID")
        sm.trigger(createTrigger(T.MOVE_TO_A))
        sm.trigger(createTrigger(T.MOVE_TO_B))
        sm.trigger(createTrigger(T.END))
        assertEquals(S.TERMINAL_STATE, sm.state.getId())
        assertThrows<StateMachineConfigurationException> { sm.start() }
    }
}
