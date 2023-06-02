package statemachine.configuration.transition

import statemachine.action.Action
import statemachine.guard.Guard
import statemachine.transition.Transition

interface TransitionsConfiguration<S, T> {

    /**
     * Adds a definition of a Transition between states in the StateMachine
     */
    fun add(transition: Transition<S, T>)

    /**
     * Adds a definition of a Transition between states in the StateMachine
     */
    fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionAction: Action<S, T>? = null)
    fun add(source: S, target: S, trigger: T?, guard: Guard<S, T>, transitionActions: List<Action<S, T>>)

    /**
     * Returns the configured [Transition] definitions
     * used by the [statemachine.configuration.StateMachineConfiguration] to configure the StateMachine
     */
    fun getTransitions(): Set<Transition<S, T>>
}
