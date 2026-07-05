dependencies {
    api(project(":ottertui-core"))
    api(project(":ottertui-widgets"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(project(":ottertui-backend-jline"))
    testRuntimeOnly(project(":ottertui-backend-lanterna"))
}
