val group = property("group") as String
val author: String = property("author") as String
val projectWebsite: String = property("website") as String
project.group = group

plugins {
    `maven-publish`
    jacoco
    kotlin("jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.18.0"
    id("java-library")
    id("org.jreleaser") version "1.12.0"
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.20"
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
        armored = true
        verify = true
    }
    project {
        author(author)
        docsUrl = projectWebsite
        website = projectWebsite
        license.set("Apache-2.0")
        description.set("An abstract implementation of a Simple Finite State Machine.")
        inceptionYear.set("2024")
        copyright.set(author)
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    namespace = "$group-$name"
                    setActive("ALWAYS")
                    applyMavenCentralRules = true
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/target/staging-deploy")
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
                url.set(projectWebsite)
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id.set("yonigev")
                        name.set(author)
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/yonigev/simple-finite-state-machine.git")
                    developerConnection.set("scm:git:ssh://github.com/yonigev/simple-finite-state-machine.git")
                    url.set(projectWebsite)
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("target/staging-deploy"))
        }
    }
}

coverallsJacoco {
    reportPath = "${project.name}/build/reports/jacoco/test/jacocoTestReport.xml"
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
