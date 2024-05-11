plugins {
    application

}

repositories {
    mavenCentral()
}

dependencies {
    implementation(lib.logback)
    implementation(lib.lombok)
    implementation(project(mapOf("path" to ":statemachine-core")))
    annotationProcessor(lib.lombok)

    testImplementation(project(mapOf("path" to ":statemachine-core")))
    testImplementation(test.junit.jupiter.api)
    testImplementation(test.junit.jupiter.engine)
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("demo.flight.FlightStateMachineApplication")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}