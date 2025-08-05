plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(libs.geantyref)
    compileOnlyApi(libs.adventure.api)
}
