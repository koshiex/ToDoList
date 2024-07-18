import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

configure<BaseAppModuleExtension> {
    baseAndroidConfig()
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.work.runtime)

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)

    implementation(libs.data.store)

    implementation(libs.system.ui.controller)
}