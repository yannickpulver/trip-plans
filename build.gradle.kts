plugins {
    //trick: for the same plugin versions in all sub-modules
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.google.gms.google-services").version("4.3.14").apply(false)
}

buildscript {
    dependencies {
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
