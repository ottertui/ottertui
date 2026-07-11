pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.9.0")
}

rootProject.name = "ottertui"

include("ottertui-core")
include("ottertui-widgets")
include("ottertui-tui")
include("ottertui-backend-jline")
include("ottertui-backend-lanterna")
include("ottertui-backend-aesh")
// FFM backend requires JDK 22+ (java.lang.foreign)
if (JavaVersion.current() >= JavaVersion.VERSION_22) {
    include("ottertui-backend-ffm")
}
include("ottertui-toolkit")
include("ottertui-examples")
include("ottertui-integration-tests")
