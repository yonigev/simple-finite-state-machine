package statemachine.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.state.State
import statemachine.trigger.Trigger

interface Transition<S, T> {
    val source: State<S>
    val target: State<S>
    val trigger: Trigger<T>
    val guard: Guard<S, T>
    val actions: Iterable<Action<S, T>>
}
