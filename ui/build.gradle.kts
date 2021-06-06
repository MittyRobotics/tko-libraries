plugins {
    kotlin("multiplatform")
}

group = "com.github.MittyRobotics"
version = "unspecified"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm()
}