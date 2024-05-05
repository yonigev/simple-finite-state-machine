package io.github.yonigev.sfsm.definition.transition

import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.guard.Guard
import io.github.yonigev.sfsm.transition.Transition

/**
 * Used to define a [state machine's](StateMachine) available transitions
 */

open class TransitionsDefiner<S, T> {
    private val transitions = mutableSetOf<Transition<S, T>>()

    fun add(sourceId: S, targetId: S, triggerId: T?, guard: Guard<S, T>, transitionAction: TransitionAction<S, T>? = null) {
        add(Transition.create(sourceId, targetId, triggerId, guard, transitionAction?.let { listOf(it) }))
    }

    fun add(transition: Transition<S, T>) {
        transitions.add(transition)
    }

    fun add(
        sourceId: S,
        targetId: S,
        triggerId: T?,
        guard: Guard<S, T>,
        transitionTransitionActions: List<TransitionAction<S, T>>,
    ) {
        add(
            Transition.create(
                sourceId,
                targetId,
                triggerId,
                guard,
                transitionTransitionActions,
            ),
        )
    }

    fun getTransitions(): Set<Transition<S, T>> {
        return transitions.toSet()
    }
}
