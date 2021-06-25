plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = "com.github.MittyRobotics"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm()
}