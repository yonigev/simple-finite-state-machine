package statemachine.factory

import org.slf4j.LoggerFactory
import statemachine.DefaultStateMachine
import statemachine.StateMachine
import statemachine.configuration.StateMachineConfiguration
import statemachine.configuration.StateMachineConfigurationException
import statemachine.state.State

/**
 * A default StateMachineFactory implementation
 */
class DefaultStateMachineFactory<S, T>(override val configuration: StateMachineConfiguration<S, T>) :
    StateMachineFactory<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun create(id: String): StateMachine<S, T> {
        if (!configuration.finalized) {
            log.warn("State Machine configuration not yet finalized! finalizing now.")
            configuration.finalize()
        }
        val states = configuration.states
        val transitions = configuration.transitionMap

        return DefaultStateMachine(id, states, transitions)
    }
}