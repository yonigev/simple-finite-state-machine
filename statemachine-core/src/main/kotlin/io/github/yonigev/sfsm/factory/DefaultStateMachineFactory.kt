package io.github.yonigev.sfsm.factory

import io.github.yonigev.sfsm.DefaultStateMachine
import io.github.yonigev.sfsm.StateMachine
import io.github.yonigev.sfsm.context.DefaultStateMachineContext
import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.state.State
import org.slf4j.LoggerFactory

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
