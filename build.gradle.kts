plugins {
    id("doburoku.base")
    id("doburoku.publishing")
    alias(libs.plugins.indra.publishing.sonatype)
}

val projectVersion: String by project
version = projectVersion
