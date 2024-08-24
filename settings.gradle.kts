pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
    id("com.gradle.enterprise") version("3.16.2")

}

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}

include(":statemachine-core")
include(":statemachine-examples")
include(":statemachine-examples:flight")
include(":statemachine-examples:login")
include(":statemachine-uml-plugin")

dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            library("statemachine-core-latest", "io.github.yonigev.sfsm:statemachine-core:+")
            library("logging-facade", "io.github.microutils:kotlin-logging-jvm:3.0.4")
            library("logback", "ch.qos.logback:logback-classic:1.3.14")
            library("lombok", "org.projectlombok:lombok:1.18.26")
            library("classGraph", "io.github.classgraph:classgraph:4.8.172")
        }
        create("test") {
            library("kotlin-test-junit5", "org.jetbrains.kotlin:kotlin-test-junit5:1.9.24")

            library("junit-jupiter-engine", "org.junit.jupiter:junit-jupiter-engine:5.9.1")
            library("junit-jupiter-api", "org.junit.jupiter:junit-jupiter-api:5.9.1")
            library("junit-bom", "org.junit:junit-bom:5.9.1")
        }
    }
}