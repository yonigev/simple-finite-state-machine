package statemachine.configuration

import statemachine.state.BaseState
import statemachine.state.State
import statemachine.transition.Transition
import statemachine.transition.TransitionMap

/**
 * Defines and validates a State Machine's configuration
 * Contains all necessary properties to build a [statemachine.StateMachine]
 */
class DefaultStateMachineConfiguration<S, T> : StateMachineConfiguration<S, T> {
    lateinit var initialState: State<S>
    lateinit var states: Set<State<S>>
    lateinit var transitions: TransitionMap<S, T>

    companion object {
        operator fun <S, T> invoke(
            initialState: State<S>,
            states: Set<State<S>>,
            transitionMap: TransitionMap<S, T>
        ): DefaultStateMachineConfiguration<S, T> {
            validate(initialState, states, transitionMap)
            return DefaultStateMachineConfiguration<S, T>()
                .also {
                    it.initialState = initialState
                    it.states = states
                    it.transitions = transitionMap
                }
        }

        private fun <S, T> validate(initialState: State<S>, states: Set<State<S>>, transitionMap: TransitionMap<S, T>) {
            val transitions = transitionMap.getTransitionSet()
            val transitionStates = transitions.map { setOf(it.source, it.target) }.flatten().toHashSet()

            if (transitions.firstOrNull { it.source.getId() == it.target.getId() } != null) {
                throw StateMachineConfigurationException("Simple Finite State Machine does not currently support self transitions.")
            }

            // validate states
            if (transitionStates != states && (transitionStates + states).size > states.size) {
                "Some transition states: $transitionStates are not defined in the states set: $states"
                    .let { throw StateMachineConfigurationException(it) }
            }

        }
    }

    override fun initial(initialState: State<S>): StateMachineConfiguration<S, T> {
        this.initialState = initialState
        return this
    }

    override fun states(vararg states: State<S>): StateMachineConfiguration<S, T> {
        this.states = states.toSet()
        return this
    }

    override fun transitions(vararg transitions: Transition<S, T>): StateMachineConfiguration<S, T> {
        this.transitions = TransitionMap(transitions.toSet())
        return this
    }
}
