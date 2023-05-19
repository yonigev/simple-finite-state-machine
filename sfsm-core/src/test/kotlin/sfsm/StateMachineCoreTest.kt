/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package sfsm

import org.junit.jupiter.api.Test
import statemachine.StateMachine
import statemachine.configuration.DefaultStateMachineConfiguration
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.ofPredicate
import statemachine.trigger.Trigger
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StateMachineCoreTest {
    private val positiveGuard = ofPredicate<S, T> { true }
    private val negativeGuard = ofPredicate<S, T> { false }

    @Test
    fun testStateMachineConstruction() {
        val config = DefaultStateMachineConfiguration<S, T>()
        val factory = DefaultStateMachineFactory(config)

        config.configureStates().also {
            it.setInitial(S.INITIAL)
            it.add(S.STATE_A)
            it.add(S.STATE_B)
            it.setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).also {
            it.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            it.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            it.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
        }

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        sm.stop()
    }

    @Test
    fun testBasicStateMachineFlow() {
        val config = DefaultStateMachineConfiguration<S, T>()
        val factory = DefaultStateMachineFactory(config)

        config.configureStates().also {
            it.setInitial(S.INITIAL)
            it.add(S.STATE_A)
            it.add(S.STATE_B)
            it.add(S.STATE_C)
            it.setTerminal(S.TERMINAL_STATE)
        }

        (config.configureTransitions() as (DefaultTransitionsConfiguration)).also {
            it.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
            it.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
            // Transition to STATE_C should be blocked, as the guard always returns false
            it.add(S.STATE_B, S.STATE_C, T.END, negativeGuard)
            it.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
        }

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
}

fun createTrigger(t: T): Trigger<T> {
    return object : Trigger<T> {
        override fun getId(): T {
            return t
        }

        override fun getArguments(): Map<*, *> {
            return mapOf<String, String>()
        }
    }
}

enum class S {
    INITIAL, STATE_A, STATE_B, STATE_C, TERMINAL_STATE
}

enum class T {
    MOVE_TO_A, MOVE_TO_B, MOVE_TO_C, END
}