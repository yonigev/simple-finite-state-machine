plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("ch.qos.logback:logback-classic:1.3.0")
    implementation(project(mapOf("path" to ":statemachine-core")))
    testImplementation(project(mapOf("path" to ":statemachine-core")))
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.9.1")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("demo.flight.FlightStateMachineApplication")
}
