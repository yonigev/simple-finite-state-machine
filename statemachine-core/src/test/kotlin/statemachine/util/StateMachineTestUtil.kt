package statemachine.util

import statemachine.definition.DefaultStateMachineDefinition
import statemachine.definition.StateMachineDefinition
import statemachine.guard.Guard.Companion.ofPredicate
import statemachine.trigger.Trigger

val positiveGuard = ofPredicate<S, T> { true }
val negativeGuard = ofPredicate<S, T> { false }

/**
 * A Basic state machine definition creator - for tests
 */
class StateMachineTestUtil {
    companion object {
        fun createDefinition(): StateMachineDefinition<S, T> {
            val definition = DefaultStateMachineDefinition<S, T>()
            definition.defineStates().apply {
                setInitial(S.INITIAL)
                simple(S.STATE_A)
                simple(S.STATE_B)
                simple(S.STATE_C)
                terminal(S.TERMINAL_STATE)
            }

            definition.defineTransitions().apply {
                add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                // Transition to STATE_C should be blocked, as the guard always returns false
                add(S.STATE_B, S.STATE_C, T.MOVE_TO_C, negativeGuard)
                add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
            }
            return definition
        }

        fun createTrigger(t: T): Trigger<T> {
            return object : Trigger<T> {
                override fun getTriggerId(): T {
                    return t
                }
            }
        }
    }
}
enum class S {
    INITIAL, STATE_A, STATE_B, STATE_C, TERMINAL_STATE
}

enum class T {
    MOVE_TO_A, MOVE_TO_B, MOVE_TO_C, MOVE_TO_C_OR_END, FORCE_MOVE_TO_C, END
}
