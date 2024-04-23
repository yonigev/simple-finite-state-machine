package statemachine.configuration.transition

import statemachine.action.TransitionAction
import statemachine.guard.Guard
import statemachine.transition.Transition

interface TransitionsDefiner<S, T> {

    /**
     * Adds a definition of a Transition between states in the StateMachine
     */
    fun add(transition: Transition<S, T>)

    /**
     * Adds a definition of a Transition between state in the StateMachine
     */
    fun add(sourceId: S, targetId: S, triggerId: T?, guard: Guard<S, T>, transitionAction: TransitionAction<S, T>? = null)
    fun add(sourceId: S, targetId: S, triggerId: T?, guard: Guard<S, T>, transitionTransitionActions: List<TransitionAction<S, T>>)

    /**
     * Returns the defined [Transitions](Transition)
     * used by the [statemachine.configuration.StateMachineConfiguration] to define the StateMachine
     */
    fun getTransitions(): Set<Transition<S, T>>
}
