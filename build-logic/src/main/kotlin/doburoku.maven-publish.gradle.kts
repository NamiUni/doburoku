import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    id("java-library")
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    configure(
        JavaLibrary(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = true,
        )
    )

    pom {
        name.set("doburoku")
        description.set("An Adventure-focused library inspired by Moonshine.")
        url.set("https://github.com/NamiUni/doburoku")
        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/licenses/mit-license.php")
            }
        }
        developers {
            developer {
                id.set("NamiUni")
                name.set("Namiu (うにたろう)")
            }
        }
        scm {
            url.set("https://github.com/NamiUni/doburoku/")
            connection.set("scm:git:git://github.com/NamiUni/doburoku.git")
            developerConnection.set("scm:git:ssh://git@github.com/NamiUni/doburoku.git")
        }
    }
}
