package io.github.yonigev.sfsm

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.action.TransitionAction.Companion.create
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory
import io.github.yonigev.sfsm.guard.Guard
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.transition.TransitionContext
import io.github.yonigev.sfsm.trigger.Trigger
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.StateMachineTestUtil
import io.github.yonigev.sfsm.util.StatefulTrigger
import io.github.yonigev.sfsm.util.T
import io.github.yonigev.sfsm.util.positiveGuard
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StateMachineTest {
    private val stateMachineFactory = DefaultStateMachineFactory<S, T>()

    @Test
    fun testBasicStateMachineFlow() {
        val stateMachineDefiner = StateMachineTestUtil.basicStateMachineDefiner()

        val sm: StateMachine<S, T> =
            stateMachineFactory.create("TEST_ID", stateMachineDefiner.getDefinition()).also { it.start() }
        assertEquals(S.INITIAL, sm.state.id)

        sm.trigger(Trigger.ofId(T.MOVE_TO_A)).also { assertEquals(sm.state.id, S.STATE_A) }
        sm.trigger(Trigger.ofId(T.MOVE_TO_B)).also { assertEquals(sm.state.id, S.STATE_B) }
        sm.trigger(Trigger.ofId(T.MOVE_TO_C)).also { assertNotEquals(sm.state.id, S.STATE_C) }
        sm.trigger(Trigger.ofId(T.END)).also { assertEquals(sm.state.id, S.TERMINAL_STATE) }
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

        sm.trigger(Trigger.ofId(T.MOVE_TO_A)).also { assertEquals(S.STATE_A, sm.state.id) }
        sm.trigger(Trigger.ofId(T.MOVE_TO_B)).also { assertEquals(S.STATE_B, sm.state.id) }

        sm.trigger(Trigger.ofId(T.MOVE_TO_C_OR_END)).also { assertEquals(S.STATE_C, sm.state.id) }
        sm = stateMachineFactory.createStarted("SHOULD_END", stateMachineDefinition)
        shouldEnd = true
        sm.apply {
            trigger(Trigger.ofId(T.MOVE_TO_A))
            trigger(Trigger.ofId(T.MOVE_TO_B))
            trigger(Trigger.ofId(T.MOVE_TO_C_OR_END))
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
        sm.trigger(Trigger.ofId(T.MOVE_TO_A))
        assertEquals(S.TERMINAL_STATE, sm.state.id)
    }

    @Test
    fun testStateMachineTransition_affectedByTrigger() {
        val statefulTriggerMessage = StatefulTrigger(T.MOVE_TO_A, "This is an example of a trigger's state")
        val emptyStringStatefulTriggerMessage = StatefulTrigger(T.MOVE_TO_A, "")

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                val statefulTriggerGuard = Guard.ofContextualPredicate<S, T> {
                    (it.trigger as StatefulTrigger).state.isNotBlank()
                }

                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, statefulTriggerGuard)
                definer.add(S.STATE_A, S.STATE_B, null, positiveGuard)
                definer.add(S.STATE_B, S.TERMINAL_STATE, null, positiveGuard)
            }
        }

        val stateMachineDefinition = stateMachineDefiner.getDefinition()
        // assert that transition is allowed based on the trigger's state / payload
        val sm1 = stateMachineFactory.createStarted("TEST_ID", stateMachineDefinition)
        val sm2 = stateMachineFactory.createStarted("TEST_ID_2", stateMachineDefinition)
        assertEquals(S.INITIAL, sm1.state.id)
        sm1.trigger(statefulTriggerMessage)
        sm2.trigger(emptyStringStatefulTriggerMessage)
        assertEquals(S.TERMINAL_STATE, sm1.state.id)
        assertNotEquals(S.TERMINAL_STATE, sm2.state.id)
    }

    @Test
    fun testStateMachineTransition_contextAwareGuard() {
        // Allow transition only if the state machine's ID matches a certain ID.
        // this is to ensure the state machine's context is accessed and read properly when a Guard calculates a transition
        val stateMachineContextAwareGuard = object : Guard<S, T> {
            override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
                return transitionContext.stateMachineContext.id == "TEST_ID"
            }
        }
        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, stateMachineContextAwareGuard)
                definer.add(S.STATE_A, S.STATE_B, null, positiveGuard)
                definer.add(S.STATE_B, S.TERMINAL_STATE, null, positiveGuard)
            }
        }

        val stateMachineDefinition = stateMachineDefiner.getDefinition()
        // assert that transition is allowed based on the trigger's state / payload
        val sm1 = stateMachineFactory.createStarted("TEST_ID", stateMachineDefinition)
        val sm2 = stateMachineFactory.createStarted("TEST_ID_2", stateMachineDefinition)
        assertEquals(S.INITIAL, sm1.state.id)
        sm1.trigger(Trigger.ofId(T.MOVE_TO_A))
        sm2.trigger(Trigger.ofId(T.MOVE_TO_A))
        assertEquals(S.TERMINAL_STATE, sm1.state.id)
        assertNotEquals(S.TERMINAL_STATE, sm2.state.id)
    }

    @Test
    fun testStateMachineTransition_contextPropertyAwareGuard() {
        val setPropertyAction = create<S, T> {
            it.stateMachineContext.setProperty("PROPERTY", true)
        }
        // this guard allows transition only when the value of key "PROPERTY" is true
        val stateMachineContextAwareGuard = object : Guard<S, T> {

            override fun allow(transitionContext: TransitionContext<S, T>): Boolean {
                return transitionContext.stateMachineContext.getPropertyOrDefault("PROPERTY", false) as Boolean
            }
        }

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A)
                definer.simple(S.STATE_B)
                definer.terminal(S.TERMINAL_STATE)
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, setPropertyAction)
                definer.add(S.STATE_A, S.STATE_B, null, stateMachineContextAwareGuard)
                definer.add(S.STATE_B, S.TERMINAL_STATE, null, positiveGuard)
            }
        }

        val stateMachineDefinition = stateMachineDefiner.getDefinition()
        val sm = stateMachineFactory.createStarted("TEST_ID", stateMachineDefinition)
        assertEquals(S.INITIAL, sm.state.id)
        sm.trigger(Trigger.ofId(T.MOVE_TO_A))
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

            // test out all difference methods for Transition creation
            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                definer.add(
                    sourceId = S.STATE_A,
                    targetId = S.STATE_B,
                    triggerId = T.MOVE_TO_B,
                    guard = positiveGuard,
                    transitionActions = listOf(
                        create { output.add(1) },
                        create { output.add(2) },
                        create { output.add(3) },
                    ),
                )
                definer.add(
                    Transition.create(
                        sourceId = S.STATE_B,
                        targetId = S.STATE_C,
                        triggerId = T.FORCE_MOVE_TO_C,
                        transitionActions = listOf(
                            create { output.add(1) },
                            create { output.add(2) },
                            create { output.add(3) },
                        ),
                    ),
                )
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
            }
        }

        val sm: StateMachine<S, T> = stateMachineFactory.createStarted("TEST_ID", stateMachineDefiner.getDefinition())
            .apply {
                trigger(Trigger.ofId(T.MOVE_TO_A))
                trigger(Trigger.ofId(T.MOVE_TO_B))
                trigger(Trigger.ofId(T.FORCE_MOVE_TO_C))
            }
        assertEquals(S.STATE_C, sm.state.id)
        assertEquals(listOf(1, 2, 3, 1, 2, 3), output)
    }

    @Test
    fun testStateMachine_stateActions_RunningSequentially() {
        val output = mutableListOf<Int>()

        val stateMachineDefiner = object : StateMachineDefiner<S, T>() {
            override fun defineStates(definer: StatesDefiner<S, T>) {
                definer.setInitial(S.INITIAL)
                definer.simple(S.STATE_A, StateAction.create { output.add(2) }, StateAction.create { output.add(3) })
                definer.simple(S.STATE_B, StateAction.create { output.add(5) }, StateAction.create { output.add(6) })
                definer.choice(S.STATE_B, StateAction.create { output.add(5) }, StateAction.create { output.add(6) })
                definer.terminal(S.TERMINAL_STATE, StateAction.create { output.add(8) })
            }

            override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, create { output.add(1) })
                definer.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard, create { output.add(4) })
                definer.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard, create { output.add(7) })
                definer.add(S.STATE_B, S.STATE_A, T.MOVE_TO_A, positiveGuard, create { output.add(7) })
            }
        }

        stateMachineFactory.createStarted("TEST", stateMachineDefiner.getDefinition())
            .also {
                assertEquals(S.INITIAL, it.state.id)
            }
            .apply {
                trigger(Trigger.ofId(T.MOVE_TO_A))
                trigger(Trigger.ofId(T.MOVE_TO_B))
                trigger(Trigger.ofId(T.END))
            }

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8), output)
    }
}
