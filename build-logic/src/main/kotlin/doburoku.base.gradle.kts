plugins {
    id("java-library")
    id("net.kyori.indra")
    id("net.kyori.indra.checkstyle")
    id("net.kyori.indra.licenser.spotless")
}

indra {
    mitLicense()
    javaVersions {
        minimumToolchain(21)
        target(21)
    }
    github("NamiUni", "doburoku")
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:-processing")
}

dependencies {
    checkstyle(libs.checkstyle)
    compileOnlyApi(libs.jspecify)
}
