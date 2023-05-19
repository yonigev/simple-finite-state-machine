package statemachine.transition

import statemachine.action.Action
import statemachine.guard.Guard

interface Transition<S, T> {
    val source: S
    val target: S
    val trigger: T
    val guard: Guard<S, T>
    val actions: Iterable<Action<S, T>>
}
