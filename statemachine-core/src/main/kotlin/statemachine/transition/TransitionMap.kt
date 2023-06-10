package statemachine.transition

/**
 * A set of transitions as part of a State Machine's configuration.
 * built as a map of (<state,trigger> -> transition) for faster access
 */
class TransitionMap<S, T>(transitions: Set<Transition<S, T>>) {
    private val transitionsMap: Map<Pair<S, T?>, Collection<Transition<S, T>>> = buildTransitionMap(transitions)

    fun getTransitions(stateId: S, trigger: T?): Collection<Transition<S, T>> {
        val key = Pair(stateId, trigger)
        return transitionsMap[key] ?: listOf()
    }

    // Build an immutable map of transitions
    private fun buildTransitionMap(transitions: Set<Transition<S, T>>): Map<Pair<S, T?>, Collection<Transition<S, T>>> {
        val mutableMap = mutableMapOf<Pair<S, T?>, List<Transition<S, T>>>()
        transitions.forEach { t ->
            val key = Pair(t.source, t.trigger)
            mutableMap.computeIfPresent(key) { k, v -> mutableMap[k].also { (v as MutableList).add(t) } }
            mutableMap.computeIfAbsent(key) { _ -> mutableListOf(t) }
        }
        return mutableMap.toMap()
    }
}
