
plugins {
    kotlin("jvm") version "1.8.10"
     id("io.github.yonigev.statemachine.uml.generator") version "0.0.1"
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("ch.qos.logback:logback-classic:1.3.0")
    implementation("io.github.yonigev:statemachine-uml-annotation:0.0.1")
    implementation("io.github.yonigev:statemachine-core:0.0.1")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(project(mapOf("path" to ":statemachine-core")))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

application {
    mainClass.set("login.AppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
