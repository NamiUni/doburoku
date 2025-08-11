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
        "internal",
        "spi",
        "standard"
    ).forEach {
        api(project(":doburoku-$it"))
    }
}
