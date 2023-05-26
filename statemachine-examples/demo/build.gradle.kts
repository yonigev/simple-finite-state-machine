plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    project(":statemachine-core")
    implementation(project(mapOf("path" to ":statemachine-core")))
    testImplementation(project(mapOf("path" to ":statemachine-core")))

    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.14.0")
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
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("java.example.App")
}
