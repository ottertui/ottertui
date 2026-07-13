plugins {
    id("ottertui.java-conventions")
}

tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }

dependencies {
    testImplementation(project(":ottertui-core"))
    testImplementation(project(":ottertui-widgets"))
    testImplementation(project(":ottertui-tui"))
    testImplementation(project(":ottertui-toolkit"))
    testImplementation(project(":ottertui-examples"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
