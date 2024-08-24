package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.uml.dot.mapper.DetailedStateMachineUmlMapper
import io.github.yonigev.sfsm.uml.dot.mapper.SimpleStateMachineUmlMapper
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

fun generateUml(definition: StateMachineDefinition<*, *>,
                outputDir: String,
                mode: Mode = Mode.SIMPLE): Pair<File, File> {

    val umlOutputDir = Path(outputDir, definition.name)
    val uml = definition.toDotUmlString(mode)
    val name = "${mode.toString().lowercase()}_${definition.name}"

    val dotFile = writeToDotFile(uml, umlOutputDir, name)
    val svgFile = writeSvgFromDotFile(dotFile, umlOutputDir, name)
    return Pair(dotFile, svgFile)
}

private fun writeToDotFile(uml: String, outputDir: Path, name: String?): File {
    Files.createDirectories(outputDir)
    val file = File("${outputDir.toAbsolutePath()}/$name.dot")
    file.writeText(uml)
    return file
}

/**
 * Read the generated DOT file to generate an SVG output
 */
private fun writeSvgFromDotFile(dotFile: File, directory: Path, name: String): File {
    val svgFile = File(directory.pathString, "$name.svg")
    val command = listOf("dot", "-Tsvg", "-o", svgFile.absolutePath, dotFile.absolutePath)

    val process = ProcessBuilder()
        .command(command)
        .redirectErrorStream(true)
        .start()
    val exitCode = process.waitFor()

    return if (exitCode == 0) {
        println("SVG file generated: ${svgFile.absolutePath}")
        svgFile
    } else {
        throw IOException("Error: Failed to generate SVG file")
    }
}

internal fun StateMachineDefinition<*, *>.toDotUmlString(mode: Mode = Mode.SIMPLE): String {
    return when(mode) {
        Mode.SIMPLE -> SimpleStateMachineUmlMapper().map(this)
        Mode.DETAILED -> DetailedStateMachineUmlMapper().map(this)
    }
}
