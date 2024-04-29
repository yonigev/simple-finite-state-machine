plugins {
    id("java")
    kotlin("jvm") version "1.8.10"
    `maven-publish`
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            version = "0.0.1"
            groupId = "io.github.yonigev"
            artifactId = "statemachine-uml-annotation"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}