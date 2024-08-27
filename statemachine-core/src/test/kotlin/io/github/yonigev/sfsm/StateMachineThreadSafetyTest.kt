package io.github.yonigev.sfsm

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory
import io.github.yonigev.sfsm.trigger.Trigger
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class StateMachineThreadSafetyTest {
    private val factory = DefaultStateMachineFactory<S, T>()

    @Test
    fun `Test basic state machine multi threaded flow`() {
        val concurrency = 100
        val stateMachineDefiner = counterStateMachineDefiner()
        val sm: StateMachine<S, T> = factory.createStarted("TEST_ID", stateMachineDefiner.getDefinition())
        val pool = Executors.newFixedThreadPool(concurrency)

        val task = Runnable {
            sm.trigger(Trigger.ofId(T.MOVE_TO_A))
            sm.trigger(Trigger.ofId(T.MOVE_TO_B))
            sm.trigger(Trigger.ofId(T.MOVE_TO_C))
            sm.trigger(Trigger.ofId(T.END))
        }

        assertEquals(S.INITIAL, sm.state.id)
        for (i in 1..concurrency) pool.submit(task)

        pool.shutdown()
        pool.awaitTermination(5, TimeUnit.SECONDS)
        val count = sm.context.getProperty("count")
        assertEquals(S.TERMINAL_STATE, sm.state.id)
        assertEquals(10, count)
    }

    private fun counterStateMachineDefiner(): StateMachineDefiner<S, T> {
        val definer = object : StateMachineDefiner<S, T>("Simple Counter State Machine") {
            val incrementingStateAction = StateAction.create<S, T> {
                var count: Int = it.getProperty("count") as Int
                count++
                it.setProperty("count", count)
            }
            val incrementingTransitionAction = TransitionAction.create<S, T> {
                var count: Int = it.stateMachineContext.getPropertyOrDefault("count", 0) as Int
                count++
                it.stateMachineContext.setProperty("count", count)
            }

            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.apply {
                    setInitial(S.INITIAL)
                    simple(S.STATE_A, incrementingStateAction, incrementingStateAction)
                    simple(S.STATE_B, incrementingStateAction, incrementingStateAction)
                    simple(S.STATE_C, incrementingStateAction, incrementingStateAction)
                    terminal(S.TERMINAL_STATE)
                }
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.apply {
                    add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, null, incrementingTransitionAction)
                    add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, null, incrementingTransitionAction)
                    add(S.STATE_B, S.STATE_C, T.MOVE_TO_C, null, incrementingTransitionAction)
                    add(S.STATE_C, S.TERMINAL_STATE, T.END, null, incrementingTransitionAction)
                }
            }
        }
        return definer
    }
}
