package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import org.slf4j.LoggerFactory

/**
 * This class is used to define a state machine's states and transitions
 */
abstract class StateMachineDefiner<S, T>(private val name: String? = null) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val statesDefiner: StatesDefiner<S, T> = StatesDefiner()
    private val transitionsDefiner: TransitionsDefiner<S, T> = TransitionsDefiner()
    private val validator = DefinitionValidator<S, T>()

    protected abstract fun defineStates(definer: StatesDefiner<S, T> = this.statesDefiner)

    protected abstract fun defineTransitions(definer: TransitionsDefiner<S, T> = this.transitionsDefiner)

    fun getDefinition(): StateMachineDefinition<S, T> {
        if (statesDefiner.getStates().isEmpty()) {
            defineStates()
        }

        if (transitionsDefiner.getTransitions().isEmpty()) {
            defineTransitions()
        }
        val states = statesDefiner.getStates()
        val transitions = transitionsDefiner.getTransitions()
        validateDefinition(states, transitions)
        return StateMachineDefinition(this.name ?: this.javaClass.simpleName, states, transitions)
    }

    private fun validateDefinition(states: Set<State<S, T>>, transition: Set<Transition<S, T>>) {
        validator.validateStates(states)
        validator.validateTransitions(states, transition)
    }
}
