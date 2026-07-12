pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ottertui"

include("ottertui-core")
include("ottertui-widgets")
include("ottertui-tui")
include("ottertui-backend-jline")
include("ottertui-backend-lanterna")
include("ottertui-backend-aesh")
include("ottertui-backend-ffm")
include("ottertui-toolkit")
include("ottertui-examples")
include("ottertui-integration-tests")

// Auto-configure git hooks on first Gradle invocation per clone.
// Uses ProcessBuilder (not Gradle exec) to avoid interfering with daemon builds.
val hooksDir = rootDir.resolve(".githooks")
if (hooksDir.isDirectory) {
    val current = try {
        ProcessBuilder("git", "config", "--get", "core.hooksPath")
            .directory(rootDir)
            .start()
            .inputStream.bufferedReader().readLine()
    } catch (_: Exception) { null }
    if (current != hooksDir.absolutePath) {
        ProcessBuilder("git", "config", "core.hooksPath", hooksDir.absolutePath)
            .directory(rootDir)
            .inheritIO()
            .start()
            .waitFor()
    }
}
