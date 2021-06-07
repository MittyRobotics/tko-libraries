rootProject.name = "tko-libraries"

include("motion")
include("core")
include("simulation")
include("ui")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}