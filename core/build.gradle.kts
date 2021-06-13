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
    mingwX64()
    jvm()


    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:kmath-core:0.3.0-dev-8")
                api("space.kscience:kmath-for-real:0.3.0-dev-8")
            }
        }
    }
}