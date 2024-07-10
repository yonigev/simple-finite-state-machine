package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.uml.dot.mapper.DetailedStateMachineUmlMapper
import io.github.yonigev.sfsm.uml.dot.mapper.SimpleStateMachineUmlMapper
import org.junit.jupiter.api.Test

class StateMachineUmlMapperTest {
    @Test
    fun testSimpleUmlMapper() {
        val mapper = SimpleStateMachineUmlMapper()
        val smDefinition = createDummyStateMachineDefinitionWithActions(TEST_MACHINE_NAME)
        val umlString = mapper.map(smDefinition)

        assert(umlString.isNotBlank())
        assertUmlEquals(SIMPLE_SM_UML, umlString)
    }

    @Test
    fun testDetailedUmlMapper() {
        val mapper = DetailedStateMachineUmlMapper()
        val smDefinition = createDummyStateMachineDefinitionWithActions(TEST_MACHINE_NAME)
        val umlString = mapper.map(smDefinition)

        assert(umlString.isNotBlank())
        assertUmlEquals(DETAILED_SM_UML_WITH_ACTIONS, umlString)
    }
}