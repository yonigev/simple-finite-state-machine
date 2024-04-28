package statemachine.uml.annotation

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import statemachine.definition.StateMachineDefiner
import statemachine.definition.StateMachineDefinition
import java.io.File
import java.lang.reflect.Constructor

class UmlAnnotationScanner {
    fun scanAnnotatedStateMachineDefinitions(classPath: String): Collection<StateMachineDefinition<*,*>> {
        return findAnnotatedClasses(classPath)
            .map { instantiateStateMachineDefiner(it).getDefinition() }
    }

    private fun findAnnotatedClasses(classPath: String?): List<ClassInfo> {
        val classGraph = ClassGraph().enableAllInfo()

        if (!classPath.isNullOrBlank()) {
            classGraph.overrideClasspath(classPath)
        }

        try {
            return classGraph.scan()
                .getClassesWithAnnotation(Uml::class.qualifiedName)
                .standardClasses?.toList()
                ?.also { filterValidStateMachineDefiners(it) } ?: listOf()
        } catch (e: Exception) {
            throw Exception("Failed to find @Uml annotated classes", e)
        }
    }

    private fun instantiateStateMachineDefiner(definerClassInfo: ClassInfo): StateMachineDefiner<*, *> {
        return (definerClassInfo.loadClass()
            .constructors.first { it.parameters.isEmpty() }.newInstance() as StateMachineDefiner<*,*>)
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