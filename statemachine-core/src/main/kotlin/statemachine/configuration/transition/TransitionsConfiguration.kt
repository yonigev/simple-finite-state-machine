package statemachine.configuration.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.transition.Transition

interface TransitionsConfiguration<S, T> {

    /**
     * Adds a Transition definition between states in the StateMachine
     */
    fun add(transition: Transition<S, T>)
    fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionAction: Action<S, T>? = null)
    fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionActions: List<Action<S, T>>)

    /**
     * Returns the configured transition definitions
     */
    fun getTransitions(): Set<Transition<S, T>>
}
