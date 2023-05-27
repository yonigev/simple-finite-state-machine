plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0" }

rootProject.name = "simple-finite-state-machine"
include("statemachine-core")
include(":statemachine-examples")
include(":statemachine-examples:demo")
findProject(":statemachine-core:statemachine-core")?.name = "statemachine-core"
