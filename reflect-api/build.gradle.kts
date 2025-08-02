plugins {
    id("doburoku.base")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(libs.geantyref)
    api(libs.adventure.api)
}
