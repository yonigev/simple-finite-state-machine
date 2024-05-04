package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.guard.Guard

/**
 * Defines the conditions required for a [io.github.yonigev.sfsm.StateMachine] to transition from state [source] to state [target]
 * if the machine receives the defined [trigger] (or the trigger is null) - the transition will happen only if the [guard]
 * allows it. if it does - the defined [actions] will run.
 */
interface Transition<S, T> {
    val source: S
    val target: S
    val trigger: T?
    val guard: Guard<S, T>
    val actions: Iterable<TransitionAction<S, T>>

    companion object {
        fun <S, T> create(
            source: S,
            target: S,
            trigger: T?,
            guard: Guard<S, T> = Guard.ofPredicate { true },
            transitionActions: List<TransitionAction<S, T>>? = null,
        ): Transition<S, T> {
            return object : Transition<S, T> {
                override val source: S
                    get() = source
                override val target: S
                    get() = target
                override val trigger: T?
                    get() = trigger
                override val guard: Guard<S, T>
                    get() = guard
                override val actions: List<TransitionAction<S, T>>
                    get() = when (transitionActions) {
                        null -> emptyList()
                        else -> transitionActions
                    }
            }
        }
    }
}
