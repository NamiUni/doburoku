plugins {
    id("doburoku.base")
    id("doburoku.testing")
}

val projectVersion: String by rootProject
version = projectVersion

dependencies {
    implementation(projects.doburokuAnnotation)
    compileOnly(libs.google.auto.service.annotations)
    annotationProcessor(libs.google.auto.service)

    testImplementation(libs.google.compile.testing)
}
