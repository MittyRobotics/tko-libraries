plugins {
    kotlin("multiplatform")
}

group = "com.github.MittyRobotics"
version = "0.1.0"

repositories {
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