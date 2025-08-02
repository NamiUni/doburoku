@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "doburoku"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")
}

sequenceOf(
    "annotations",
    "annotation-processor",
    "bom",
    "api",
    "reflect-api",
    "reflect-core",
    "reflect-internal",
    "reflect-standard",
    "reflect-minimessage"
).forEach {
    include("doburoku-$it")
    project(":doburoku-$it").projectDir = file(it)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
