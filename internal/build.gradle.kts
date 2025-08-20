plugins {
    id("doburoku.base")
    id("doburoku.testing")
    id("doburoku.maven-publish")
}

val projectVersion: String by project
version = projectVersion

dependencies {
    compileOnlyApi(projects.doburokuApi)
}
