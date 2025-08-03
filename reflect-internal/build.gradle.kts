plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    api(projects.doburokuApi)
    api(projects.doburokuReflectApi)
    api(libs.geantyref)
}

tasks.javadoc {
    options.quiet()
}
