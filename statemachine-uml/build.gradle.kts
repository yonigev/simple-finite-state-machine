plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    kotlin("jvm") version "1.8.10"
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(gradleApi())
    implementation(project(mapOf("path" to ":statemachine-core")))
    implementation("io.github.classgraph:classgraph:4.8.172")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

group = "io.github.yonigev"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("GenerateStateMachineUml") {
            id = "${group}.statemachine.uml.generator"
            implementationClass = "statemachine.uml.plugin.UmlGeneratorPlugin"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.test {
    useJUnitPlatform()
}