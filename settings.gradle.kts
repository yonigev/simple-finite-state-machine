pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "simple-finite-state-machine"
include(":statemachine-core")
include(":statemachine-examples")
include(":statemachine-examples:flight")
include(":statemachine-examples:login")
include(":statemachine-uml-plugin")
include(":statemachine-uml-annotation")
