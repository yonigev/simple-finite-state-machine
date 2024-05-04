package io.github.yonigev.sfsm.util

import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.guard.Guard.Companion.ofPredicate
import io.github.yonigev.sfsm.trigger.Trigger

val positiveGuard = ofPredicate<S, T> { true }
val negativeGuard = ofPredicate<S, T> { false }

/**
 * A Basic state machine definition creator - for tests
 */
class StateMachineTestUtil {
    companion object {
        fun basicStateMachineDefiner(): StateMachineDefiner<S, T> {
            val definer = object : StateMachineDefiner<S, T>() {
                override fun defineStates(definer: StatesDefiner<S, T>) {
                    definer.setInitial(S.INITIAL)
                    definer.simple(S.STATE_A)
                    definer.simple(S.STATE_B)
                    definer.simple(S.STATE_C)
                    definer.terminal(S.TERMINAL_STATE)
                }

                override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
                    definer.add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                    definer.add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                    // Transition to STATE_C should be blocked, as the guard always returns false
                    definer.add(S.STATE_B, S.STATE_C, T.MOVE_TO_C, negativeGuard)
                    definer.add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
                }
            }
            return definer
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
