plugins {
    id("ottertui.java-conventions")
    application
}

tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }

application {
    mainClass = "com.ottertui.examples.AllExamplesApp"
}

dependencies {
    implementation(project(":ottertui-core"))
    implementation(project(":ottertui-widgets"))
    implementation(project(":ottertui-tui"))
    implementation(project(":ottertui-toolkit"))
    implementation(project(":ottertui-backend-jline"))
    implementation(project(":ottertui-backend-lanterna"))
    runtimeOnly(libs.logback.classic)
}
