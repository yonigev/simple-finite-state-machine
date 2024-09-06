package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.guard.Guard

/**
 * Defines the conditions required for a [io.github.yonigev.sfsm.StateMachine] to transition from state [sourceId] to state [targetId]
 * if the machine receives the defined [triggerId] (or the trigger is null) - the transition will happen only if the [guard]
 * allows it. if it does - the defined [actions] will run.
 */
interface Transition<S, T> {
    val sourceId: S
    val targetId: S
    val triggerId: T?
    val guard: Guard<S, T>?
    val actions: List<TransitionAction<S, T>>

    companion object {
        fun <S, T> create(
            sourceId: S,
            targetId: S,
            triggerId: T?,
            guard: Guard<S, T>? = Guard.ofPredicate { true },
            transitionActions: List<TransitionAction<S, T>>? = null,
        ): Transition<S, T> {
            return object : Transition<S, T> {

                override val sourceId: S
                    get() = sourceId
                override val targetId: S
                    get() = targetId
                override val triggerId: T?
                    get() = triggerId
                override val guard: Guard<S, T>?
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
