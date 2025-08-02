plugins {
    id("doburoku.base")
    id("doburoku.testing")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuReflectApi)
    implementation(projects.doburokuReflectInternal)
}
