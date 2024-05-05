plugins {
    kotlin("jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.18.0"
    id("java-library")
    `maven-publish`
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        ktfmt()
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
repositories {
    mavenCentral()
    mavenLocal()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            version = "0.0.1"
            groupId = "io.github.yonigev.sfsm"
            artifactId = "statemachine-core"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.12")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
