package io.github.yonigev.sfsm.uml

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import io.github.yonigev.sfsm.definition.StateMachineDefinition
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

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
        get() = "${project.projectDir.absolutePath}/uml"


    @TaskAction
    fun action() {
        val sourceSetFiles = project.sourceSetFiles
        val definitions: Collection<StateMachineDefinition<*, *>> = UmlAnnotationScanner(sourceSetFiles).scan()

        for (definition in definitions) {
            val outputDir = Path.of(project.umlResourceDir, definition.name)
            val uml = definition.toDotUmlString()
            val dotFile = writeToDotFile(uml, outputDir, definition.name)
            writeSvgFromDotFile(dotFile, outputDir, definition.name)
        }
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
    private fun writeSvgFromDotFile(dotFile: File, directory: Path, name: String) {
        val svgFile = File(directory.pathString, "$name.svg")
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