package statemachine.transition

import statemachine.action.TransitionAction
import statemachine.guard.Guard

/** A Default Transition implementation */
open class DefaultTransition<S, T>(
    override val source: S,
    override val target: S,
    override val trigger: T?,
    override val guard: Guard<S, T>,
    override val actions: Iterable<TransitionAction<S, T>>,
) : Transition<S, T> {
    override fun toString(): String {
        return "DefaultTransition(source=$source, target=$target, trigger=$trigger, guard=$guard, actions=$actions)"
    }

    companion object {
        fun <S, T> create(
            source: S,
            target: S,
            trigger: T?,
            guard: Guard<S, T>,
            transitionAction: TransitionAction<S, T>? = null,
        ): DefaultTransition<S, T> {
            return DefaultTransition(
                source,
                target,
                trigger,
                guard,
                when (transitionAction) {
                    null -> emptyList()
                    else -> listOf(transitionAction)
                },
            )
        }
    }
}
