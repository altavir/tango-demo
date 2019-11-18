import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "scientifik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/tango-controls/jtango/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.tango-controls:JTango:9.5.17"){
        exclude("nanocontainer")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}