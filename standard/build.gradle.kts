plugins {
    id("doburoku.base")
    id("doburoku.testing")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuCore)
    api(libs.geantyref)

    compileOnlyApi(libs.adventure.text.minimessage)
}
