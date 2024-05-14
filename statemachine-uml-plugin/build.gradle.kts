val group = property("group") as String
val author: String = property("author") as String
val projectWebsite: String = property("website") as String
project.group = group

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
            id = "${group}.statemachine-uml-generator"
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
            url = uri(layout.buildDirectory.dir("target/staging-deploy"))
        }
    }
}
