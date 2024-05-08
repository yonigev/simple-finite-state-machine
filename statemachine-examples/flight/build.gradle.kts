plugins {
    application
    id("io.github.yonigev.sfsm.statemachine.uml.generator") version "0.0.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation(project(mapOf("path" to ":statemachine-core")))
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation(project(mapOf("path" to ":statemachine-core")))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

application {
    mainClass.set("demo.flight.FlightStateMachineApplication")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}