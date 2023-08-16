plugins {
    // trick: for the same plugin versions in all sub-modules
    kotlin("multiplatform") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("com.google.gms.google-services") version "4.3.14" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
}

buildscript {
    dependencies {
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(libs.moko.resources.generator)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
