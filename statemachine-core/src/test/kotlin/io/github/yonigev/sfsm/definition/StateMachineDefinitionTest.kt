package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.util.StateMachineTestUtil
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class StateMachineDefinitionTest {

    @Test
    fun testStateMachineDefinitionCreation() {
        val definer = StateMachineTestUtil.basicStateMachineDefiner()
        val definition = definer.getDefinition()
        assertContains(definition.name, "StateMachineDefiner")
        assertTrue(definition.states.isNotEmpty())
        assertTrue(definition.transitions.isNotEmpty())
    }
}
