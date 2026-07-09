dependencies {
    api(project(":ottertui-core"))
    api(project(":ottertui-widgets"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(project(":ottertui-backend-jline"))
    testRuntimeOnly(project(":ottertui-backend-lanterna"))
    testRuntimeOnly(project(":ottertui-backend-aesh"))
    // FFM backend requires JDK 22+
    if (JavaVersion.current() >= JavaVersion.VERSION_22) {
        testRuntimeOnly(project(":ottertui-backend-ffm"))
    }
}
