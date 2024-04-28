package statemachine.uml

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import statemachine.definition.StateMachineDefinition
import statemachine.state.State
import statemachine.transition.Transition
import statemachine.uml.annotation.UmlAnnotationScanner
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

open class UmlMapperTask : DefaultTask() {

    private val Project.sourceSets: SourceSetContainer
        get() = (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

    private val Project.mainSourceSets: SourceSet
        get() = this.sourceSets.getByName(MAIN_SOURCE_SET_NAME)



    @TaskAction
    fun action() {

        val definitions = UmlAnnotationScanner()
            .scanAnnotatedStateMachineDefinitions(getSourceSetDirs())

        for (definition in definitions) {
            val uml = toUml(definition)
            val dotFile = writeToDotFile("./", definition, uml)
            writeSvgFromDotFile(definition, dotFile)
        }
    }

    private fun getSourceSetDirs(): String {
        return project.mainSourceSets.output.classesDirs.asPath
    }

    fun toUml(definition: StateMachineDefinition<*, *>): String {
        val name = definition.name
        val states = definition.states.joinToString(" \n") { mapState(it) }
        val transitions = definition.transitions.joinToString(" \n") { mapTransition(it) }
        return """
                |digraph StateMachine {
                |graph [label="$name", fontsize=16]
                |node [shape = circle]; // Set node shape to circle

                |// Define states
                |$states
            
                |// Define transitions between states
                |$transitions
             |}
            """.trimMargin()
    }

    private fun mapState(state: State<*, *>): String {
        return if (state.type == State.PseudoStateType.INITIAL || state.type == State.PseudoStateType.TERMINAL) {
            "${state.id} [shape=doublecircle];"
        } else {
            "${state.id};"
        }
    }

    private fun mapTransition(transition: Transition<*, *>): String {
        return "${transition.source} -> ${transition.target} [label=\"${transition.trigger}\"];"
    }

    private fun writeToDotFile(directory: String, definition: StateMachineDefinition<*, *>, uml: String): File {
        Files.createDirectories(Path.of(directory))
        val file = File("${directory}/${definition.name}.dot")
        file.writeText(uml)
        return file
    }

    private fun writeSvgFromDotFile(definition: StateMachineDefinition<*, *>, dotFile: File) {
        val directory = dotFile.parentFile
        val svgFile = File("${directory.path}/${definition.name}.svg")
        val command = listOf("dot", "-Tsvg", "-o", svgFile.absolutePath, dotFile.absolutePath)

        val process = ProcessBuilder()
            .command(command)
            .redirectErrorStream(true)
            .start()
        val exitCode = process.waitFor()
        if (exitCode == 0) {
            println("SVG file generated: ${svgFile.absolutePath}")
        } else {
            println("Error: Failed to generate SVG file")
        }
    }
}