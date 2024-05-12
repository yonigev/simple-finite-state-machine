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
    testImplementation(test.kotlin.test.junit5)
    testImplementation(test.junit.jupiter.engine)
    implementation(lib.logging.facade)
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
        license.set("Apache-2.0")
        description.set("An abstract implementation of a Simple Finite State Machine.")
        inceptionYear.set("2024")
        copyright.set("Jonathan Geva")
    }
    deploy {
        maven {
            mavenCentral {
                this.create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/publications/staging-deploy")
                    dryrun.set(true)
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
                url.set("https://github.com/yonigev/simple-finite-state-machine")
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
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
            val release = uri(layout.buildDirectory.dir("publications/releases"))
            val snapshot = uri(layout.buildDirectory.dir("publications/snapshots"))
            val staging = uri(layout.buildDirectory.dir("publications/staging-deploy"))
            url = staging
        }
    }
}
