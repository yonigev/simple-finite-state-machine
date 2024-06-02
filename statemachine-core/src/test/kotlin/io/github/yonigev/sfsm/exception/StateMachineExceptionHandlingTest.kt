package io.github.yonigev.sfsm.exception

import io.github.yonigev.sfsm.StateMachine
import io.github.yonigev.sfsm.StateMachineException
import io.github.yonigev.sfsm.action.TransitionAction.Companion.create
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.StateMachineDefinitionException
import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory
import io.github.yonigev.sfsm.guard.Guard.Companion.ofPredicate
import io.github.yonigev.sfsm.trigger.Trigger
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.StateMachineTestUtil
import io.github.yonigev.sfsm.util.T
import io.github.yonigev.sfsm.util.positiveGuard
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StateMachineExceptionHandlingTest {

    private val stateMachineFactory = DefaultStateMachineFactory<S, T>()

    @Test
    fun testExceptionThrownInGuardIsPropagated() {
        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.apply {
                    setInitial(S.INITIAL)
                    simple(S.STATE_A)
                    simple(S.STATE_B)
                    terminal(S.TERMINAL_STATE)
                }
            }
            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.apply {
                    add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, ofPredicate { throw Exception("Exception Throwing Guard") })
                    add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                }
            }
        }

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", stateMachineDefiner.getDefinition())
        val e = assertThrows<StateMachineException> { sm.trigger(Trigger.ofId(T.MOVE_TO_A)) }
        assertEquals(e.getStateMachineId(), sm.id)
        assertContains(e.message, sm.id)
    }

    @Test
    fun testExceptionThrownInActionIsPropagated() {
        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.apply {
                    setInitial(S.INITIAL)
                    simple(S.STATE_A)
                    simple(S.STATE_B)
                    terminal(S.TERMINAL_STATE)
                }
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.apply {
                    add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, create<S, T> { throw Exception("Exception thrown in a Transition Action") })
                    add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                }
            }
        }

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", stateMachineDefiner.getDefinition())
        val e = assertThrows<StateMachineException> { sm.trigger(Trigger.ofId(T.MOVE_TO_A)) }
        assertEquals(e.getStateMachineId(), sm.id)
    }

    @Test
    fun testStateMachineThrowsException_whenStoppedAndTriggered() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        val sm: StateMachine<S, T> = stateMachineFactory.create("TEST_ID", definition)
        assertEquals(S.INITIAL, sm.state.id)
        assertThrows<StateMachineDefinitionException> { sm.trigger(Trigger.ofId(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStartedInTerminalState() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", definition)
        sm.trigger(Trigger.ofId(T.MOVE_TO_A))
        sm.trigger(Trigger.ofId(T.MOVE_TO_B))
        sm.trigger(Trigger.ofId(T.END))
        assertEquals(S.TERMINAL_STATE, sm.state.id)
        assertThrows<StateMachineDefinitionException> { sm.start() }
    }
}
