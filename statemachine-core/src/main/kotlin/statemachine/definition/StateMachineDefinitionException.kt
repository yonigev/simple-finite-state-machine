package statemachine.definition

/**
 * A general [Exception] indicating the [statemachine.StateMachine] was not defined properly.
 */
class StateMachineDefinitionException(s: String?) : IllegalArgumentException(s)
