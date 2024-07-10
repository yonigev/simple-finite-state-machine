package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.definition.StateMachineDefinition

/**
 * Mapping a State Machine's definition into a UML string
 */
interface StateMachineUmlMapper {

    fun map(definition: StateMachineDefinition<*, *>): String
}