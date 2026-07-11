tasks.named<JacocoReport>("jacocoTestReport") {
    classDirectories.setFrom(files())
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    classDirectories.setFrom(files())
}

dependencies {
    api(project(":ottertui-core"))
    implementation(libs.lanterna)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
