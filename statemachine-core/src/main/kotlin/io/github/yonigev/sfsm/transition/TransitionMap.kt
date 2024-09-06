package io.github.yonigev.sfsm.transition

/**
 * A set of transitions that are part of a State Machine's definition.
 * Structured as a map for faster access
 */
class TransitionMap<S, T> (transitions: Set<Transition<S, T>>) {
    private val map: Map<Pair<S, T?>, Collection<Transition<S, T>>> = buildTransitionMap(transitions)

    fun getTransitions(stateId: S, trigger: T?): Collection<Transition<S, T>> {
        val key = Pair(stateId, trigger)
        return map[key] ?: listOf()
    }

    // Build an immutable map of transitions
    private fun buildTransitionMap(transitions: Set<Transition<S, T>>): Map<Pair<S, T?>, Collection<Transition<S, T>>> {
        val mutableMap = mutableMapOf<Pair<S, T?>, List<Transition<S, T>>>()
        transitions.forEach { t ->
            val key = Pair(t.sourceId, t.triggerId)
            mutableMap.computeIfPresent(key) { _, v -> (v as MutableList).add(t); v }
            mutableMap.computeIfAbsent(key) { _ -> mutableListOf(t) }
        }
        return mutableMap.toMap()
    }
}
