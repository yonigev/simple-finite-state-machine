package statemachine.uml.annotation

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import statemachine.definition.StateMachineDefiner
import statemachine.definition.StateMachineDefinition
import java.lang.reflect.Constructor

class UmlAnnotationScanner {
    fun scanAnnotatedStateMachineDefinitions(): Collection<StateMachineDefinition<*,*>> {
        val definitions = mutableListOf<StateMachineDefinition<*,*>>()
        val annotatedStateMachineDefiners = findAnnotatedClasses()

        for (definer in annotatedStateMachineDefiners) {
            val instance = (definer.loadClass()
                .constructors.first { it.parameters.isEmpty() }.newInstance() as StateMachineDefiner<*,*>)
            definitions.add(instance.getDefinition())
        }

        return definitions
    }

    private fun findAnnotatedClasses(): List<ClassInfo> {
        val classGraph = ClassGraph().enableAllInfo()
        try {
            return classGraph.scan()
                .getClassesWithAnnotation(Uml::class.qualifiedName)
                .standardClasses?.toList()
                ?.also { filterValidStateMachineDefiners(it) } ?: listOf()
        } catch (e: Exception) {
            throw Exception("Failed to find @Uml annotated classes", e)
        }
    }

    private fun filterValidStateMachineDefiners(annotatedClasses: List<ClassInfo>): List<ClassInfo> {

        return annotatedClasses
            .filter { it.extendsSuperclass(StateMachineDefiner::class.qualifiedName) }
            .filter { retrieveEmptyConstructors(it) != null }
    }

    private fun retrieveEmptyConstructors(stateMachineDefiner: ClassInfo): Constructor<*>? {
        return stateMachineDefiner.loadClass().constructors.firstOrNull { it.parameters.isEmpty() }
    }
}