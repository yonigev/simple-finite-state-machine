package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import org.slf4j.LoggerFactory

open class DefinitionValidator<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun validateStates(states: Set<State<S, T>>): Boolean {
        val initialStates = states.filter { State.PseudoStateType.INITIAL == it.type }
        val endStates = states.filter { State.PseudoStateType.TERMINAL == it.type }

        // Validate there is 1 INITIAL state
        if (initialStates.size != 1) {
            "Invalid number of INITIAL states!: $initialStates".also {
                log.error(it); throw StateMachineDefinitionException(it)
            }
        }

        // Validate there is at least 1 END state
        if (endStates.isEmpty()) {
            "Invalid number of END states!: $endStates".also {
                log.error(it); throw StateMachineDefinitionException(it)
            }
        }

        return true
    }

    fun validateTransitions(states: Set<State<S, T>>, transitions: Set<Transition<S, T>>): Boolean {
        val stateIds = states.map { it.id }
        val statesMap = states.associateBy { it.id }

        val transitionStates: Set<S> = transitions.map { setOf(it.source, it.target) }.flatten().toSet()
        if (transitionStates != stateIds && (transitionStates + stateIds).size > states.size) {
            "Some transition states: $transitionStates are not defined in the states set: $states".let {
                throw StateMachineDefinitionException(
                    it,
                )
            }
        }

        // Validate transition with a CHOICE state source
        for (choiceState in states.filter { it.type == State.PseudoStateType.CHOICE }) {
            val choiceSourceTransitions = transitions.filter { it.source == choiceState.id }
            if (choiceSourceTransitions.size < 2) {
                throw StateMachineDefinitionException("Choice state with $choiceSourceTransitions.size outgoing transitions")
            }
        }

        // Validate transition with a non-choice state source
        for (nonChoiceStateTransition in transitions.filter { statesMap[it.source]!!.type != State.PseudoStateType.CHOICE }) {
            val similarTransitions = transitions.filter {
                it.source == nonChoiceStateTransition.source &&
                    it.trigger == nonChoiceStateTransition.trigger
            }
            if (similarTransitions.size > 1) {
                throw StateMachineDefinitionException(
                    "Found multiple transitions with non-choice source state: ${nonChoiceStateTransition.source} " +
                        "and trigger: ${nonChoiceStateTransition.trigger}",
                )
            }
        }

        return true
    }
}
