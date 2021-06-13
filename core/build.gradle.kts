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
                api("space.kscience:kmath-core:0.2.1")
                api("space.kscience:kmath-commons:0.2.1")
                api("space.kscience:kmath-for-real:0.2.1")
            }
        }
    }
}