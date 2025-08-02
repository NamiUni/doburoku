plugins {
    id("java-library")
}

tasks.test {
    jvmArgs("-Xshare:off")
}

dependencies {
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.platform)
}
