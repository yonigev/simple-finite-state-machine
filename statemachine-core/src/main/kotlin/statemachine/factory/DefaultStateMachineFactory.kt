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
open class DefaultStateMachineFactory<S, T>(override val definition: StateMachineDefinition<S, T>) :
    StateMachineFactory<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun createStarted(id: String): StateMachine<S, T> {
        return create(id).apply { start() }
    }

    open fun setupInitialStateMachineContext(id: String): StateMachineContext<S, T> {
        val initial = definition.states.first { State.PseudoStateType.INITIAL == it.type }
        return DefaultStateMachineContext(id, initial)
    }

    override fun create(id: String): StateMachine<S, T> {
        if (!definition.processed) {
            log.warn("State Machine definition not yet processed! processing now.")
            definition.process()
        }

        val transitions = definition.transitions
        val initialContext = setupInitialStateMachineContext(id)
        return DefaultStateMachine(definition.states, transitions, initialContext)
    }
}
