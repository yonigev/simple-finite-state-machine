package statemachine.uml.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec

class UmlGeneratorPlugin:  Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create("GenerateStateMachineUml", JavaExec::class.java) {
            it.args()
            it.mainClass.set("statemachine.uml.plugin.UmlGeneratorPlugin")
        }
    }
}
