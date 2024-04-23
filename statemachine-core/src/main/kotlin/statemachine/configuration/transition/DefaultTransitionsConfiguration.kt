package statemachine.configuration.transition

import statemachine.action.TransitionAction
import statemachine.guard.Guard
import statemachine.transition.Transition

/**
 * A default implementation for the [TransitionsConfiguration]
 */
class DefaultTransitionsConfiguration<S, T> : TransitionsConfiguration<S, T> {
    private val transitions = mutableSetOf<Transition<S, T>>()

    override fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionAction: TransitionAction<S, T>?) {
        add(Transition.create(source, target, trigger, guard, transitionAction?.let { listOf(it) }))
    }

    override fun add(transition: Transition<S, T>) {
        transitions.add(transition)
    }

    override fun add(
        source: S,
        target: S,
        trigger: T?,
        guard: Guard<S, T>,
        transitionTransitionActions: List<TransitionAction<S, T>>,
    ) {
        add(
            Transition.create(
                source,
                target,
                trigger,
                guard,
                transitionTransitionActions,
            ),
        )
    }

    override fun getTransitions(): Set<Transition<S, T>> {
        return transitions.toSet()
    }
}
