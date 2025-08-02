plugins {
    id("doburoku.base")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuApi)
    api(projects.doburokuReflectApi)
    api(libs.geantyref)
}
