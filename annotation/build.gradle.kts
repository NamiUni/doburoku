plugins {
    id("doburoku.base")
    id("doburoku.maven-publish")
}

val projectVersion: String by rootProject
version = projectVersion
