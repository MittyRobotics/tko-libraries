plugins {
    kotlin("multiplatform") version "1.5.10" apply false
}

group = "com.github.MittyRobotics"
version = "0.1.0"

allprojects {
    repositories {
        maven {
            url = uri("https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven")
            metadataSources {
                mavenPom()
                ignoreGradleMetadataRedirection()

            }
        }
        mavenCentral()
    }
}