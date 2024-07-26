plugins {
    id("android-app-convention")
    id("todolist-serialization")
    id("telegram-sender")
}

    telegram {
        token.set(providers.environmentVariable("TODO_TG_TOKEN"))
        chatId.set(providers.environmentVariable("TODO_TG_CHAT_ID"))
    }

android {
    defaultConfig {
        applicationId = "com.kionavani.todotask"
        versionCode = 1
        versionName = "1.0"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        val clientId: String = providers.environmentVariable("TODO_CLIENT_ID").get()
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "\"$clientId\""

        val baseUrl: String =  providers.environmentVariable("TODO_BASE_URL").get()
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        val oAuthToken: String =  providers.environmentVariable("TODO_OAUTH_TOKEN").get()
        buildConfigField("String", "OAUTH_TOKEN", "\"$oAuthToken\"")
    }
}

dependencies {
    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.core.ktx)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}