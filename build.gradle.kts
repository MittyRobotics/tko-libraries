plugins {
    kotlin("multiplatform") version "1.5.10" apply false
    `maven-publish`
}

group = "com.github.MittyRobotics"
version = "0.1.0"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
