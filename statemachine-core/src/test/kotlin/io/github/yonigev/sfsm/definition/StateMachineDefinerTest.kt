package io.github.yonigev.sfsm.definition

import io.github.yonigev.sfsm.util.StateMachineTestUtil
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StateMachineDefinerTest {

    @Test
    fun testStateMachineDefinitionCreation() {
        val definer = StateMachineTestUtil.basicStateMachineDefiner()
        val definition = definer.getDefinition()
        assertContains(definition.name, "StateMachineDefiner")
        assertTrue(definition.states.isNotEmpty())
        assertTrue(definition.transitions.isNotEmpty())
    }

    @Test
    fun testStateMachineDefinition_assignsDefinitionName_whenNotExplicitlyDefined() {
        val testName = "test definer name"
        val definer = StateMachineTestUtil.basicStateMachineDefiner(testName)
        val definition = definer.getDefinition()
        assertEquals(testName, definition.name)
    }
}
