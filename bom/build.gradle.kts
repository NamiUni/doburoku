plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    sequenceOf(
        "annotation",
        "annotation-processor",
        "api",
        "core",
        "internal",
        "standard"
    ).forEach {
        api(project(":doburoku-$it"))
    }
}
