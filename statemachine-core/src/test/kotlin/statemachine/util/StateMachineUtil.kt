package statemachine.util

import statemachine.configuration.DefaultStateMachineConfiguration
import statemachine.configuration.StateMachineConfiguration
import statemachine.configuration.transition.DefaultTransitionsConfiguration
import statemachine.guard.Guard.Companion.createGuard
import statemachine.trigger.Trigger

val positiveGuard = createGuard<S, T> { true }
val negativeGuard = createGuard<S, T> { false }

/**
 * A Basic state machine configuration creator - for tests
 */
class StateMachineUtil {
    companion object {
        fun createConfig(): StateMachineConfiguration<S, T> {
            val config = DefaultStateMachineConfiguration<S, T>()
            config.configureStates().apply {
                setInitial(S.INITIAL)
                add(S.STATE_A)
                add(S.STATE_B)
                add(S.STATE_C)
                setTerminal(S.TERMINAL_STATE)
            }

            (config.configureTransitions() as (DefaultTransitionsConfiguration)).apply {
                add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard)
                add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
                // Transition to STATE_C should be blocked, as the guard always returns false
                add(S.STATE_B, S.STATE_C, T.END, negativeGuard)
                add(S.STATE_B, S.TERMINAL_STATE, T.END, positiveGuard)
            }
            return config
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
    }
}
enum class S {
    INITIAL, STATE_A, STATE_B, STATE_C, TERMINAL_STATE
}

enum class T {
    MOVE_TO_A, MOVE_TO_B, MOVE_TO_C, FORCE_MOVE_TO_C, END
}
