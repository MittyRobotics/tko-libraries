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
    mingwX64 {
        val main by compilations.getting
        val libOpenBLAS by main.cinterops.creating {
            headers("$projectDir/src/nativeInterop/cinterop/OpenBLAS/include/lapacke.h", "$projectDir/src/nativeInterop/cinterop/OpenBLAS/include/cblas.h")
            extraOpts("-libraryPath", "$projectDir/src/nativeInterop/cinterop/OpenBLAS/lib")
        }
    }
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                api("org.ejml:ejml-all:0.40")
            }
        }
    }
}