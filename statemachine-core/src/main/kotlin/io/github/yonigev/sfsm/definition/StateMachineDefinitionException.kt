package io.github.yonigev.sfsm.definition

/**
 * A general [Exception] indicating the [io.github.yonigev.sfsm.StateMachine] was not defined properly.
 */
class StateMachineDefinitionException(s: String?) : IllegalArgumentException(s)
