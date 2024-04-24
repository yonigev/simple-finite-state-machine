package statemachine.definition

import org.slf4j.LoggerFactory
import statemachine.definition.state.StatesDefiner
import statemachine.definition.transition.TransitionsDefiner

/**
 * This class is used to define a state machine's states and transitions
 */
abstract class StateMachineDefiner<S, T> {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val statesDefiner: StatesDefiner<S, T> = StatesDefiner()
    private val transitionsDefiner: TransitionsDefiner<S, T> = TransitionsDefiner()
    private val validator = DefinitionValidator<S, T>()

    protected abstract fun defineStates(definer: StatesDefiner<S, T> = this.statesDefiner)

    protected abstract fun defineTransitions(definer: TransitionsDefiner<S, T> = this.transitionsDefiner)

    fun getDefinition(): StateMachineDefinition<S, T> {
        val states = statesDefiner.getStates().let { it.ifEmpty { defineStates() }; statesDefiner.getStates() }
        val transitions = transitionsDefiner.getTransitions().let { it.ifEmpty { defineTransitions() }; transitionsDefiner.getTransitions() }
        validator.validateStates(states)
        validator.validateTransitions(states, transitions)
        return StateMachineDefinition(states, transitions)
    }
}
