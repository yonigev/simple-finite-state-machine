package statemachine.configuration

/**
 * A general [Exception] indicating the [statemachine.StateMachine] was not configured properly.
 */
class StateMachineConfigurationException(s: String?) : IllegalArgumentException(s)
