package statemachine.transition

import statemachine.guard.Guard


interface Transition<S, T> {
    fun getSource(): S
    fun getTarget(): S
    fun getTrigger(): T
    fun getGuard(): Guard<S, T>

    fun attemptTransition(): Boolean
}