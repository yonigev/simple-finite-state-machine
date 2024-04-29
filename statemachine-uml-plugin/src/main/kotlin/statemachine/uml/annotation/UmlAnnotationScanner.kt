package statemachine.uml.annotation

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import statemachine.definition.StateMachineDefiner
import statemachine.definition.StateMachineDefinition
import java.io.File
import java.lang.reflect.Constructor
import java.net.URL
import java.net.URLClassLoader
import kotlin.streams.toList

class UmlAnnotationScanner(private val classPath: Set<File> = setOf()) {
    fun scanAnnotatedStateMachineDefinitions(): Collection<StateMachineDefinition<*, *>> {
        return findAnnotatedClasses()
            .map { instantiateStateMachineDefiner(it).getDefinition() }
    }

    private fun findAnnotatedClasses(): List<ClassInfo> {
        val classLoader = getClassLoader()

        val classGraph = ClassGraph()
            .enableAllInfo()
            .overrideClasspath(classPathFilesAsPathString(this.classPath))
            .addClassLoader(classLoader)

        try {
            return classGraph.scan()
                .getClassesWithAnnotation(Uml::class.qualifiedName).standardClasses
                .filter { isValidStateMachineDefiner(it) }

        } catch (e: Exception) {
            throw Exception("Failed to find @Uml annotated classes", e)
        }
    }
    private fun getClassLoader(): ClassLoader {
        val urls: List<URL> =
            classPath.stream().map { obj: File -> obj.toURI() }
                .map { it.toURL() }.toList()
        return URLClassLoader(urls.toTypedArray(), this.javaClass.classLoader)
    }

    private fun instantiateStateMachineDefiner(definerClassInfo: ClassInfo): StateMachineDefiner<*, *> {
        return (definerClassInfo.loadClass()
            .constructors.first { it.parameters.isEmpty() }.newInstance() as StateMachineDefiner<*, *>)
    }

    private fun isValidStateMachineDefiner(annotatedClass: ClassInfo): Boolean {
        return annotatedClass.extendsSuperclass(StateMachineDefiner::class.qualifiedName)
                && retrieveEmptyConstructors(annotatedClass) != null
    }

    private fun retrieveEmptyConstructors(stateMachineDefiner: ClassInfo): Constructor<*>? {
        return stateMachineDefiner.loadClass().constructors.firstOrNull { it.parameters.isEmpty() }
    }

    private fun classPathFilesAsPathString(classPathFiles: Set<File>): String {
        return classPathFiles.joinToString(File.pathSeparatorChar.toString())
    }
}