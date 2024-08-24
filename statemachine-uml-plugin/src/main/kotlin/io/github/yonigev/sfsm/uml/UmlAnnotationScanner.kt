package io.github.yonigev.sfsm.uml

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.uml.annotation.Uml
import java.io.File
import java.lang.reflect.Constructor
import java.net.URL
import java.net.URLClassLoader
import kotlin.streams.toList

/**
 * By leveraging the ClassGraph library, this scanner scans the given
 * ClassPath files for classes annotated with @Uml.
 * If found, those classes are instantiated to retrieve their "raw" StateMachineDefinition
 *
 * Since this is used when importing a Gradle plugin, a project classpath
 * is required here for the scanning and class loading to work.
 * Note the definers must have an empty constructor available for this to work.
 */
class UmlAnnotationScanner(private val classPath: Set<File> = setOf()) {

    /**
     * Scan for @Uml StateMachineDefiner classes that also have empty constructors
     * @param useCustomClasspath will be false during testing
     */
    fun scan(useCustomClasspath: Boolean = true): List<StateMachineDefiner<*, *>> {
        val annotatedClasses = scanAnnotatedValidClasses(useCustomClasspath)
        return annotatedClasses.map { instantiateStateMachineDefiner(it) }
    }

    private fun scanAnnotatedValidClasses(useCustomClasspath: Boolean = true): List<ClassInfo> {
        val classLoader = getClassLoader()

        val classGraph = ClassGraph()
            .enableAllInfo()
            .addClassLoader(classLoader)

        if (useCustomClasspath && this.classPath.isNotEmpty()) {
            classGraph.overrideClasspath(classPathFilesAsPathString(this.classPath))
        }

        try {
            return classGraph.scan()
                .getClassesWithAnnotation(Uml::class.qualifiedName).standardClasses
                .filter { isValidStateMachineDefiner(it) }

        } catch (e: Exception) {
            throw Exception("Failed to find @Uml annotated classes", e)
        }
    }

    /**
     * An annotated class is considered valid when it's a subclass of StateMachineDefiner
     * and has an empty constructor available.
     */
    private fun isValidStateMachineDefiner(annotatedClass: ClassInfo): Boolean {
        return annotatedClass.extendsSuperclass(StateMachineDefiner::class.qualifiedName)
                && retrieveEmptyConstructors(annotatedClass) != null
    }

    private fun getClassLoader(): ClassLoader {
        val urls: Array<URL> =
            classPath.stream()
                .map { obj: File -> obj.toURI() }
                .map { it.toURL() }
                .toList().toTypedArray()

        return URLClassLoader(urls, this.javaClass.classLoader)
    }

    private fun instantiateStateMachineDefiner(definerClassInfo: ClassInfo): StateMachineDefiner<*, *> {
        return (definerClassInfo.loadClass()
            .constructors.first { it.parameters.isEmpty() }.newInstance() as StateMachineDefiner<*, *>)
    }

    private fun retrieveEmptyConstructors(stateMachineDefiner: ClassInfo): Constructor<*>? {
        return stateMachineDefiner.loadClass().constructors.firstOrNull { it.parameters.isEmpty() }
    }

    private fun classPathFilesAsPathString(classPathFiles: Set<File>): String {
        return classPathFiles.joinToString(File.pathSeparatorChar.toString())
    }
}