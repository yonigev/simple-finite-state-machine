plugins {
    kotlin("jvm") version "1.8.10"
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(lib.lombok)
    implementation(lib.logback)
    implementation(project(mapOf("path" to ":statemachine-core")))

    annotationProcessor(lib.lombok)

    testImplementation(test.kotlin.test.junit5)
    testImplementation(test.junit.jupiter.engine)
    testImplementation(project(mapOf("path" to ":statemachine-core")))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("login.AppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
