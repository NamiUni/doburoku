plugins {
    id("java-library")
    id("doburoku.base")
    id("net.kyori.indra.publishing")
}

signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
}

indra {
    publishSnapshotsTo("sonatype", "https://central.sonatype.com/repository/maven-snapshots/")
    configurePublications {
        pom {
            developers {
                developer {
                    id.set("NamiUni")
                    name.set("Namiu (うにたろう)")
                }
            }
        }
    }
}
