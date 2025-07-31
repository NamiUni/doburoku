plugins {
    id("doburoku.base")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    compileOnlyApi(libs.adventure.api)
}
