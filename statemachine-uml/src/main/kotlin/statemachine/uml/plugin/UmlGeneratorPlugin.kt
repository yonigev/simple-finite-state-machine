package statemachine.uml.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import statemachine.uml.UmlMapperTask


open class UmlGeneratorPlugin : Plugin<Project> {
// Need to see if it's best practice to bring the annotation/code with a plugin, or just ask users to add a dependency, and if so, get rid of
    // duplication
    override fun apply(project: Project) {

        project.dependencies {
            create("")
//            implementation(project(mapOf("path" to ":statemachine-uml")))
        }
        project.tasks.register<UmlMapperTask>("GenerateUml")
    }
}

