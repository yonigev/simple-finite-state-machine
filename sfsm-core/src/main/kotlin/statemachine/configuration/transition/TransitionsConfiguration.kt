package statemachine.configuration.transition

import statemachine.transition.Transition

interface TransitionsConfiguration<S, T> {

    /**
     * Adds a Transition definition between states in the StateMachine
     */
    fun add(transition: Transition<S, T>)

    /**
     * Returns the configured transition definitions
     */
    fun getTransitions(): Set<Transition<S, T>>
}
