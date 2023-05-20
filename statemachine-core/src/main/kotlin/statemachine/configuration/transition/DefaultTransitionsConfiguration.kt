package statemachine.configuration.transition

import org.slf4j.LoggerFactory
import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.transition.DefaultTransition
import statemachine.transition.Transition

/**
 * Used to configure [statemachine.StateMachine] [statemachine.state.State] transitions
 */
class DefaultTransitionsConfiguration<S, T> : TransitionsConfiguration<S, T> {

    private val transitions = mutableSetOf<Transition<S, T>>()
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun add(source: S, target: S, trigger: T, guard: Guard<S, T>, transitionAction: Action<S, T>? = null) {
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
        val existing = this.transitions.firstOrNull { transition.source == it.source && transition.trigger == it.trigger }
        if (existing != null) {
            log.warn(
                "Adding a transition: $transition \n" +
                    "Overriding existing transition: $existing",
            )
            this.transitions.remove(existing)
        }
        transitions.add(transition)
    }

    override fun getTransitions(): Set<Transition<S, T>> {
        return transitions.toSet()
    }
}
