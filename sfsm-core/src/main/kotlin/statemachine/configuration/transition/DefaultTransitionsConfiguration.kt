package statemachine.configuration.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition

class DefaultTransitionsConfiguration<S, T> : TransitionsConfiguration<S, T> {

    private val transitions = mutableSetOf<Transition<S, T>>()

    fun add(source: S, target: S, trigger: T, guard: Guard<S, T>, transitionAction: Action<S, T>? = null) {
        add(
            DefaultTransition(
                source, target, trigger, guard,
                when (transitionAction) {
                    null -> emptyList()
                    else -> listOf(transitionAction)
                }
            )
        )
    }
    override fun add(transition: Transition<S, T>) {
        transitions.add(transition)
    }

    override fun getTransitionDefinitions(): Set<Transition<S, T>> {
        return transitions.toSet()
    }
}