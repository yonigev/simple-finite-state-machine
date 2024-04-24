package statemachine.factory

import org.slf4j.LoggerFactory
import statemachine.DefaultStateMachine
import statemachine.StateMachine
import statemachine.context.DefaultStateMachineContext
import statemachine.context.StateMachineContext
import statemachine.definition.StateMachineDefinition
import statemachine.state.State

/**
 * A default StateMachineFactory implementation
 */
open class DefaultStateMachineFactory<S, T> : StateMachineFactory<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun createStarted(id: String, definition: StateMachineDefinition<S, T>): StateMachine<S, T> {
        return create(id, definition).apply { start() }
    }

    override fun create(
        machineId: String,
        definition: StateMachineDefinition<S, T>,
    ): StateMachine<S, T> {
        val states = definition.states
        val transitions = definition.transitions
        val initialContext = setupInitialStateMachineContext(machineId, states)
        log.info("Creating a state machine with states: $states and transitions: $transitions")
        return DefaultStateMachine(states, transitions, initialContext)
    }

    open fun setupInitialStateMachineContext(id: String, states: Set<State<S, T>>): StateMachineContext<S, T> {
        val initial = states.first { State.PseudoStateType.INITIAL == it.type }
        return DefaultStateMachineContext(id, initial)
    }
}
