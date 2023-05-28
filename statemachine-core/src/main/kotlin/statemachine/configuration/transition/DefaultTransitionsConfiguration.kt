package statemachine.configuration.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition

/**
 * Used to configure [statemachine.StateMachine] [statemachine.state.State] transitions
 */
class DefaultTransitionsConfiguration<S, T> : TransitionsConfiguration<S, T> {

    private val transitions = mutableSetOf<Transition<S, T>>()

    override fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionAction: Action<S, T>?) {
        add(
            DefaultTransition(
                source,
                target,
                trigger,
                guard,
                when (transitionAction) {
                    null -> emptyList()
                    else -> listOf(transitionAction)
                },
            ),
        )
    }

    override fun add(transition: Transition<S, T>) {
        transitions.add(transition)
    }

    override fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionActions: List<Action<S, T>>) {
        add(
            DefaultTransition(
                source,
                target,
                trigger,
                guard,
                transitionActions,
            ),
        )
    }

    override fun getTransitions(): Set<Transition<S, T>> {
        return transitions.toSet()
    }
}
