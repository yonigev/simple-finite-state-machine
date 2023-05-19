package statemachine.transition

import statemachine.action.Action
import statemachine.guard.Guard

/** A Default Transition implementation */
data class DefaultTransition<S, T>(
    override val source: S,
    override val target: S,
    override val trigger: T,
    override val guard: Guard<S, T>,
    override val actions: Iterable<Action<S, T>>,
) : Transition<S, T>
