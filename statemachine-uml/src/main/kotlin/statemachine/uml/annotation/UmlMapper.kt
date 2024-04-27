package statemachine.uml.annotation


import statemachine.definition.StateMachineDefinition
import statemachine.state.State
import statemachine.transition.Transition
import statemachine.state.State.PseudoStateType.INITIAL
import statemachine.state.State.PseudoStateType.TERMINAL
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class UmlMapper {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val directory = args[0]
            val definitions = UmlAnnotationScanner()
                .scanAnnotatedStateMachineDefinitions()
            for (definition in definitions) {
                val uml = toUml(definition)
                val dotFile = writeToDotFile(directory, definition, uml)
                writeSvgFromDotFile(definition, dotFile)
            }
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
            return if (state.type == INITIAL || state.type == TERMINAL) {
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
}