package statemachine.transition

import statemachine.state.State
import statemachine.trigger.Trigger

/**
 * A set of transitions as part of a State Machine's configuration.
 * built as a map of <state,trigger> -> transition for faster access
 */
class TransitionMap<S, T>(transitions: Set<Transition<S, T>>) {
    private val transitionsMap: Map<Pair<S, T>, Transition<S, T>> =
        transitions.associateBy({ Pair(it.source, it.trigger) }, { it })

    fun getTransition(state: S, trigger: T): Transition<S, T>? {
        val key = Pair(state, trigger)
        return transitionsMap[key]
    }

    fun getTransitionSet(): Collection<Transition<S, T>> {
        return transitionsMap.values
    }
}
