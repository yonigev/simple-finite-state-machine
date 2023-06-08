package statemachine.factory

import org.slf4j.LoggerFactory
import statemachine.DefaultStateMachine
import statemachine.StateMachine
import statemachine.configuration.StateMachineConfiguration
import statemachine.context.DefaultStateMachineContext
import statemachine.context.StateMachineContext
import statemachine.state.State

/**
 * A default StateMachineFactory implementation
 */
open class DefaultStateMachineFactory<S, T>(override val configuration: StateMachineConfiguration<S, T>) :
    StateMachineFactory<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun createStarted(id: String): StateMachine<S, T> {
        return create(id).apply { start() }
    }

    open fun setupInitialStateMachineContext(): StateMachineContext<S, T> {
        val states = configuration.stateDefinitions.map { it.state }
        val initial = states.first { State.PseudoStateType.INITIAL == it.getType() }
        return DefaultStateMachineContext(initial)
    }

    override fun create(id: String): StateMachine<S, T> {
        if (!configuration.processed) {
            log.warn("State Machine configuration not yet processed! processing now.")
            configuration.process()
        }
        val states = configuration.stateDefinitions.map { it.state }
        val transitions = configuration.transitionMap
        val initialContext = setupInitialStateMachineContext()
        return DefaultStateMachine(id, states, transitions, initialContext)
    }
}
