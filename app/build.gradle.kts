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

        val clientId: String = providers.environmentVariable("TODO_CLIENT_ID").get()
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "\"$clientId\""

        val baseUrl: String =  providers.environmentVariable("TODO_BASE_URL").get()
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        val oAuthToken: String =  providers.environmentVariable("TODO_OAUTH_TOKEN").get()
        buildConfigField("String", "OAUTH_TOKEN", "\"$oAuthToken\"")
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}