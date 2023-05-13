package statemachine.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.state.State
import statemachine.trigger.Trigger

/** A Default Transition implementation */
data class DefaultTransition<S, T>(
    override val source: State<S>,
    override val target: State<S>,
    override val trigger: Trigger<T>,
    override val guard: Guard<S, T>,
    override val actions: Iterable<Action<S, T>>,
) : Transition<S, T>
