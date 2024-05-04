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
    implementation(project(mapOf("path" to ":statemachine-uml-annotation")))
    implementation("io.github.classgraph:classgraph:4.8.172")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("GenerateStateMachineUml") {
            group = "io.github.yonigev.sfsm"
            version = "0.0.1"
            id = "${group}.statemachine.uml.generator"
            implementationClass = "${group}.uml.UmlGeneratorPlugin"
        }
    }
}