package io.github.yonigev.sfsm.uml

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class UmlAnnotationScannerTest {

    @Test
    fun testUmlAnnotationScanner_detectsUmlAnnotation() {
        val scannedStateMachineDefiners = UmlAnnotationScanner().scan()
        val definitions = scannedStateMachineDefiners.map { it.getDefinition() }
        assert(definitions.isNotEmpty())
    }

    @Test
    fun testUmlAnnotationScanner_extracts_ProperStateMachineDefinitions() {
        val scannedStateMachineDefiner = UmlAnnotationScanner().scan().first()
        val scannedStateMachineDefinition = scannedStateMachineDefiner.getDefinition()
        val dummyStateMachineDefinition = createDummyStateMachineDefinition(scannedStateMachineDefinition.name)

        val scannedDefinitionUmlString = scannedStateMachineDefinition.toDotUmlString()
        val expectedDefinitionUmlString = dummyStateMachineDefinition.toDotUmlString()
        assertEquals(scannedDefinitionUmlString, expectedDefinitionUmlString)
    }
}