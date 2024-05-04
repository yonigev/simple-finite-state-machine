package io.github.yonigev.sfsm.uml

import org.junit.jupiter.api.Test

class UmlAnnotationScannerTest {

    @Test
    fun testUmlAnnotationScanner_detectsUmlAnnotation() {
        val scannedStateMachineDefinitions = UmlAnnotationScanner().scan()
        assert(scannedStateMachineDefinitions.isNotEmpty())
    }

    @Test
    fun testUmlAnnotationScanner_extracts_ProperStateMachineDefinitions() {
        val scannedStateMachineDefinition = UmlAnnotationScanner().scan().first()
        val dummyStateMachineDefinitions = createDummyStateMachineDefinition(TEST_MACHINE_NAME)

        val scannedDefinitionUmlString = scannedStateMachineDefinition.toDotUmlString()
        val dummyDefinitionUmlString = dummyStateMachineDefinitions.toDotUmlString()
        // DummyStateMachineDefiner should be scanned
        assert(scannedDefinitionUmlString == dummyDefinitionUmlString)
    }
}