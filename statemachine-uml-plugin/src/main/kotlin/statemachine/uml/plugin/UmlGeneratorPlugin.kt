package statemachine.uml.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import statemachine.uml.GenerateUmlTask


open class UmlGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register<GenerateUmlTask>("GenerateUmll")
    }
}

