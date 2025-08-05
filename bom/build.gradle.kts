plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    sequenceOf(
        "service-common",
        "service",
        "service-internal",
        "service-minimessage"
    ).forEach {
        api(project(":doburoku-$it"))
    }
}
