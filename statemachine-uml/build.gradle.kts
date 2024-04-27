
plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("java")
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation(project(mapOf("path" to ":statemachine-core")))
    implementation("io.github.classgraph:classgraph:4.8.172")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

gradlePlugin {
    plugins {
        create("GenerateStateMachineUml") {
            id = "statemachine.uml"
            implementationClass = "statemachine.uml.plugin.GenerateStateMachineUml"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}