plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.18.0"
//    `java-library`
    `java-gradle-plugin`
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
    repositories {
        mavenLocal()
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
