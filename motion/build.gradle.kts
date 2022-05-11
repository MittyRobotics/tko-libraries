plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = "com.github.MittyRobotics"
version = "0.1.0"

repositories {
    maven("https://jitpack.io")
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm()
    mingwX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
            }
        }
    }
}