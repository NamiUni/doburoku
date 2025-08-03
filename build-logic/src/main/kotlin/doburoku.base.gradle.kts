plugins {
    id("java-library")
    id("checkstyle")
    id("com.diffplug.spotless")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:-processing")
}

dependencies {
    compileOnlyApi(libs.jspecify)
}
