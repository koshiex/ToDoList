package com.kionavani.todotask.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.kionavani.todotask.TelegramService
import com.kionavani.todotask.tasks.TelegramSenderTask
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property


class TelegramSender : Plugin<Project> {
    override fun apply(target: Project) {
        val androidComponents = target.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: throw GradleException("Android not found")

        val extension = target.extensions.create("telegram", TelegramExtension::class.java)

        val service = TelegramService(HttpClient(OkHttp))

        androidComponents.onVariants { variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)
            target.tasks.register(
                "reportTelegramApkFor${variant.name.capitalize()}",
                TelegramSenderTask::class.java,
                service
            ).configure {
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
            }
        }
    }
}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
}