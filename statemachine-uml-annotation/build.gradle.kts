plugins {
    id("java")
    kotlin("jvm") version "1.8.10"
    `maven-publish`
}

group = "io.github.yonigev"
version = "0.0.1"

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

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}