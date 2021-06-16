
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
    mingwX64{
        binaries{
            executable {
                entryPoint = "com.github.mittyrobotics.ui.main"
            }

        }
    }

    sourceSets {
        commonMain{
            dependencies {
                implementation(project(":core"))
                implementation(project(":motion"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation( "org.jfree:jfreechart:1.5.0")
            }
        }
    }
}