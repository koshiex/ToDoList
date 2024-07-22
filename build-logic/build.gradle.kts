plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("todolist-serialization") {
            id = "todolist-serialization"
            implementationClass = "SerializationConventionPlugin"
        }
        register("telegram-sender") {
            id = "telegram-sender"
            implementationClass = "com.kionavani.todotask.plugins.TelegramSender"
        }
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.agp)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.coroutines.core)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}