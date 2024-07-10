package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.uml.annotation.Uml
import io.github.yonigev.sfsm.uml.annotation.UmlGenerationType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * A Gradle task for generating Uml diagrams.
 * A diagram is generated for all StateMachineDefiner classes annotated with @Uml
 * The output is two files:
 *  * <definerClassName>.dot - a DOT syntax file
 *  * <definerClassName>.svg - an SVG viewable diagram drawing
 *  The files will be written to the root project "uml" directory.
 */
open class GenerateUmlTask : DefaultTask() {

    // Useful Gradle Project extensions
    private val Project.mainSourceSets: SourceSet
        get() = (this.extensions.getByName("sourceSets") as SourceSetContainer)
            .getByName(MAIN_SOURCE_SET_NAME)

    private val Project.sourceSetFiles: Set<File>
        get() = this.mainSourceSets.output.classesDirs.files
    private val Project.umlResourceDir: String
        get() = "${project.projectDir.absolutePath}/generated-uml"


    @TaskAction
    fun action() {
        val sourceSetFiles = project.sourceSetFiles
        val classScanner = UmlAnnotationScanner(sourceSetFiles)
        val definers = classScanner.scan().ifEmpty { classScanner.scan(false) }

        for (definer in definers) {
            val generationType = getUmlGenerationType(definer)
            val modes = getUmlGenerationModes(generationType)
            val definition = definer.getDefinition()

            modes.forEach {
                generateUml(definition, project.umlResourceDir, it)
            }
        }
    }

    /**
     * Extract the requested UML generation type
     */
    private fun getUmlGenerationType(stateMachineDefiner: StateMachineDefiner<*, *>): UmlGenerationType {
        return (stateMachineDefiner.javaClass).getAnnotation(Uml::class.java).type
    }

    /**
     * Extract the UML Generation Modes from the requested Uml Generation Type (A @Uml annotation parameter)
     * e.g. [UmlGenerationType.ALL] will turn into both [Mode.SIMPLE] and [Mode.DETAILED] modes
     */
    private fun getUmlGenerationModes(umlGenerationType: UmlGenerationType): List<Mode> {
        return when (umlGenerationType) {
            UmlGenerationType.ALL -> listOf(Mode.SIMPLE, Mode.DETAILED)
            else -> listOf(Mode.valueOf(umlGenerationType.toString()))
        }
    }
}

enum class Mode {
    /**
     * Generate a simple UML of a state machine
     */
    SIMPLE,

    /**
     * Generate a detailed UML of a state machine, including its Guards and Actions
     */
    DETAILED
}