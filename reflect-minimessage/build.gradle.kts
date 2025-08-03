plugins {
    id("doburoku.base")
    id("doburoku.testing")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuReflectCore)
    api(libs.adventure.text.minimessage)
}
