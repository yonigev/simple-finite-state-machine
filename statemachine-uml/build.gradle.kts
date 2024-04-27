plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("java")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(gradleApi())
    api(project(mapOf("path" to ":statemachine-core")))
    implementation("io.github.classgraph:classgraph:4.8.172")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

gradlePlugin {
    plugins {
        create("GenerateStateMachineUml") {
            id = "statemachine.uml.generator"
            version = "0.0.1"
            implementationClass = "statemachine.uml.plugin.UmlGeneratorPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
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