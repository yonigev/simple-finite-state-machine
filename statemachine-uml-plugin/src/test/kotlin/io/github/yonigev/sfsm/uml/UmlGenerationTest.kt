package io.github.yonigev.sfsm.uml

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class UmlGenerationTest {
    private val outputPath = "src/test/resources"

    @Test
    fun test_UmlGeneratedProperly() {
        val smDefinition = createDummyStateMachineDefinition(TEST_MACHINE_NAME)
        val (dot, svg) = generateUml(smDefinition, outputPath)

        assertUmlEquals(SIMPLE_SM_UML, dot)
        assertNotNull(svg)
        assertNotNull(dot)
    }

    @Test
    fun test_DetailedUml_GeneratedProperly() {
        val smDefinition = createDummyStateMachineDefinitionWithActions(TEST_MACHINE_NAME)
        val (dot, svg) = generateUml(smDefinition, outputPath, Mode.DETAILED)

        assertUmlEquals(DETAILED_SM_UML_WITH_ACTIONS, dot)
        assertNotNull(svg)
        assertNotNull(dot)
    }

    @AfterEach
    fun cleanup() {
        Path(outputPath).toFile().deleteRecursively()
    }
}