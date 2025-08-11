plugins {
    id("doburoku.base")
    id("doburoku.testing")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuSpi)
    implementation(projects.doburokuInternal)
    compileOnlyApi(libs.adventure.text.minimessage)
}
