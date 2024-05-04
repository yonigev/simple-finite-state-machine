package io.github.yonigev.sfsm.uml

import org.junit.jupiter.api.Test

class UmlMapperTest {
    @Test
    fun testDotUml_containsAllStates_andTransitions() {
        val smDefinition = createDummyStateMachineDefinition(TEST_MACHINE_NAME)
        val umlString = smDefinition.toDotUmlString()
        assert(umlString.isNotBlank())
        assert(umlString.contains(smDefinition.name))
        smDefinition.states.forEach { assert(umlString.contains(it.toString())) }
        smDefinition.transitions.forEach { assert(umlString.contains(it.trigger.toString())) }
    }
}