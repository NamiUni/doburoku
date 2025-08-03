plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    sequenceOf(
        "api",
        "reflect-api",
        "reflect-core",
        "reflect-internal",
        "reflect-standard",
        "reflect-minimessage"
    ).forEach {
        api(project(":doburoku-$it"))
    }
}
