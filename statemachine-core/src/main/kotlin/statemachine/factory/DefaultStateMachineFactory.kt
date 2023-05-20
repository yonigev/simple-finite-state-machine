package statemachine.factory

import org.slf4j.LoggerFactory
import statemachine.DefaultStateMachine
import statemachine.StateMachine
import statemachine.configuration.StateMachineConfiguration

/**
 * A default StateMachineFactory implementation
 */
class DefaultStateMachineFactory<S, T>(override val configuration: StateMachineConfiguration<S, T>) :
    StateMachineFactory<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun createStarted(id: String): StateMachine<S, T> {
        return create(id).apply { start() }
    }

    override fun create(id: String): StateMachine<S, T> {
        if (!configuration.processed) {
            log.warn("State Machine configuration not yet processed! processing now.")
            configuration.process()
        }
        val states = configuration.states
        val transitions = configuration.transitionMap

        return DefaultStateMachine(id, states, transitions)
    }
}
