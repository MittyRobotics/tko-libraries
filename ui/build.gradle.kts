
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

    sourceSets {
        commonMain {
            dependencies {
                implementation( "org.jfree:jfreechart:1.5.0")
                implementation(project(":core"))
            }
        }
    }
}