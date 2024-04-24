package statemachine.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import statemachine.StateMachine
import statemachine.StateMachineException
import statemachine.action.TransitionAction.Companion.create
import statemachine.definition.StateMachineDefiner
import statemachine.definition.StateMachineDefinitionException
import statemachine.definition.state.StatesDefiner
import statemachine.definition.transition.TransitionsDefiner
import statemachine.factory.DefaultStateMachineFactory
import statemachine.guard.Guard.Companion.ofPredicate
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.StateMachineTestUtil.Companion.createTrigger
import statemachine.util.T
import statemachine.util.positiveGuard
import kotlin.test.assertEquals

class StateMachineExceptionHandlingTest {

    private val stateMachineFactory = DefaultStateMachineFactory<S, T>()

    @Test
    fun testExceptionThrownInGuardIsPropagated() {
        val definer = object : StateMachineDefiner<S, T>() {
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

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", definer)
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testExceptionThrownInActionIsPropagated() {
        val definer = object : StateMachineDefiner<S, T>() {
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

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", definer)
        assertThrows<StateMachineException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStoppedAndTriggered() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner()
        val sm: StateMachine<S, T> = stateMachineFactory.create("TEST_ID", definition)
        assertEquals(S.INITIAL, sm.state.id)
        assertThrows<StateMachineDefinitionException> { sm.trigger(createTrigger(T.MOVE_TO_A)) }
    }

    @Test
    fun testStateMachineThrowsException_whenStartedInTerminalState() {
        val definition = StateMachineTestUtil.basicStateMachineDefiner()
        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", definition)
        sm.trigger(createTrigger(T.MOVE_TO_A))
        sm.trigger(createTrigger(T.MOVE_TO_B))
        sm.trigger(createTrigger(T.END))
        assertEquals(S.TERMINAL_STATE, sm.state.id)
        assertThrows<StateMachineDefinitionException> { sm.start() }
    }
}
