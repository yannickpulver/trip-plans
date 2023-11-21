import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("dev.icerock.mobile.multiplatform-resources")
    id("org.jlleitschuh.gradle.ktlint")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    // export correct artifact to use all classes of library directly from Swift
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-core:0.16.1")
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export("dev.icerock.moko:resources:0.23.0")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.image.loader)

                // Firebase
                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.content.negotiation)

                implementation(libs.uuid)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.tabNavigator)

            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.junit)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.koin.androidx.compose)
                implementation(libs.ktor.client.android)
                implementation("com.google.maps.android:maps-compose:2.13.0")
                implementation("com.google.maps.android:maps-compose-utils:2.13.0")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        @Suppress("UNUSED_VARIABLE")
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val iosX64Test by getting

        @Suppress("UNUSED_VARIABLE")
        val iosArm64Test by getting
    }
}

android {
    namespace = "com.yannickpulver.plans"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation(libs.androidx.core)
    commonMainApi(libs.mvvm.core)
    commonMainApi(libs.mvvm.compose)
    commonMainApi(libs.mvvm.flow)
    commonMainApi(libs.mvvm.flow.compose)
    commonMainApi(libs.moko.resources)
    commonMainApi(libs.moko.resources.compose) // for compose multiplatform
}

buildkonfig {
    packageName = "com.yannickpulver.plans"

    // default config is required
    defaultConfigs {
        buildConfigField(
            STRING,
            "MAPS_API_KEY",
            gradleLocalProperties(rootDir).getProperty("MAPS_API_KEY")
        )
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.yannickpulver.plans" // required
    iosBaseLocalizationRegion = "en" // optional, default "en"
}
