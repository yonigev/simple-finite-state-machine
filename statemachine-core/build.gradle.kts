project.version = "0.3.9-SNAPSHOT"
project.group = "io.github.yonigev.sfsm"

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.18.0"
    id("java-library")
    id("org.jreleaser") version "1.12.0"
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

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.14")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withJavadocJar()
    withSourcesJar()
}

jreleaser {
    gitRootSearch.set(true)
    signing {
        setActive("ALWAYS")
        armored.set(true)
    }
    project {
        this.license.set("Apache-2.0")
    }
    deploy {
        maven {
            mavenCentral {
                this.create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("target/staging-deploy")
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "statemachine-core"
            from(components["java"])
            pom {
                name.set("Simple Finite State Machine")
                description.set("An abstract implementation of a Simple Finite State Machine.")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://spdx.org/licenses/Apache-2.0.html")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/yonigev/simple-finite-state-machine.git")
                    developerConnection.set("scm:git:ssh://github.com/yonigev/simple-finite-state-machine.git")
                    url.set("https://github.com/yonigev/simple-finite-state-machine")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}
