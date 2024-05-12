project.group = "io.github.yonigev.sfsm"

plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    kotlin("jvm") version "1.8.10"
    `kotlin-dsl`
    java
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
    plugins {
        create("GenerateStateMachineUml") {
            id = "${group}.statemachine.uml.generator"
            implementationClass = "${group}.uml.UmlGeneratorPlugin"
        }
    }
}