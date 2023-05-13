package statemachine.transition

import statemachine.state.State
import statemachine.trigger.Trigger

class TransitionSet<S, T>(transitions: Set<Transition<S, T>>) {
    private val transitionsMap: Map<Pair<State<S>, Trigger<T>>, Transition<S, T>> =
        transitions.associateBy({ Pair(it.source, it.trigger) }, { it })

    fun getTransition(state: State<S>, trigger: Trigger<T>): Transition<S, T>? {
        val key = Pair(state, trigger)
        return transitionsMap[key]
    }
}
