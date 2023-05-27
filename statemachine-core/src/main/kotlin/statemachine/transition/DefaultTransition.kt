package statemachine.transition

import statemachine.action.Action
import statemachine.guard.Guard

/** A Default Transition implementation */
open class DefaultTransition<S, T>(
    override val source: S,
    override val target: S,
    override val trigger: T?,
    override val guard: Guard<S, T>,
    override val actions: Iterable<Action<S, T>>,
) : Transition<S, T> {
    override fun toString(): String {
        return "DefaultTransition(source=$source, target=$target, trigger=$trigger, guard=$guard, actions=$actions)"
    }
}
