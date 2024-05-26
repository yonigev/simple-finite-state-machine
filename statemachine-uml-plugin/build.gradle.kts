val group = property("group") as String
project.group = group

val author: String = property("author") as String
val projectWebsite: String = property("website") as String
val projectId = "${group}.statemachine-uml-generator"
val stagingRepository = "target/staging-deploy"

plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    kotlin("jvm") version "1.8.10"
    `kotlin-dsl`
    java
    signing
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(gradleApi())
    implementation(project(mapOf("path" to ":statemachine-core")))
    implementation(lib.classGraph)

    testImplementation(platform(test.junit.bom))
    testImplementation(test.junit.jupiter.engine)

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website = projectWebsite
    vcsUrl = projectWebsite
    plugins {
        create("GenerateStateMachineUml") {
            id = projectId
            implementationClass = "${group}.uml.UmlGeneratorPlugin"
            displayName = "A State Machine UML generator"
            description = "A plugin that helps you generate UML diagrams based on your sfsm state machines."
            tags = listOf("statemachine", "sfsm")
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir(stagingRepository))
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

tasks.register<Zip>("zipArtifacts") {
    val artifactsPath = uri(layout.buildDirectory.dir("${stagingRepository}/${projectId.replace(".", "/")}"))
    project.fileTree(artifactsPath).forEach { from(it.path) }
    archiveFileName.set("$projectId-$version.zip")
    destinationDirectory.set(file(layout.buildDirectory.dir("output")))
}
