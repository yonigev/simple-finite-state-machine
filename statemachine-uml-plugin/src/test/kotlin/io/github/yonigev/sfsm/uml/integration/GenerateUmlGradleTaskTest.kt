package io.github.yonigev.sfsm.uml.integration

import io.github.yonigev.sfsm.uml.GenerateUmlTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.Path

class GenerateUmlGradleTaskTest {
    private val outputPath = "src/test/resources"
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder()
            .withProjectDir(File(outputPath))
            .build()
    }

    @Test
    fun test_fullUmlGenerationFlow() {
        // Definers annotated with @Uml(UmlGenerationType.SIMPLE) or @Uml(UmlGenerationType.ALL)
        val expectedSimpleTypes = 2
        // Definers annotated with @Uml(UmlGenerationType.DETAILED) or @Uml(UmlGenerationType.ALL)
        val expectedDetailedTypes = 2
        // SVG and DOT files
        val numOfFilesPerDirectory = 2

        val outputPath = "${project.projectDir.absolutePath}/generated-uml"
        val task = project.tasks.create("GenerateUmlTask", GenerateUmlTask::class.java)

        project.pluginManager.apply(JavaPlugin::class.java)

        task.action()
        val outputDir = Path(outputPath).toFile()
        val outputFiles = outputDir.walkTopDown().filter { it.isFile }.toList()
        assert(outputFiles.size == (expectedSimpleTypes + expectedDetailedTypes) * numOfFilesPerDirectory)
    }

    @AfterEach
    fun cleanup() {
        Path(outputPath).toFile().deleteRecursively()
    }
}