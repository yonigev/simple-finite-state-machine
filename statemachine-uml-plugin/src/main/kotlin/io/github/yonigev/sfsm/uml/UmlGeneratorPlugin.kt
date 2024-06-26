package io.github.yonigev.sfsm.uml

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

@SuppressWarnings
open class UmlGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register<GenerateUmlTask>("GenerateUml")
    }
}

