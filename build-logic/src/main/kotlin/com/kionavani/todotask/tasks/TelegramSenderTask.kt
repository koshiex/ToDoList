package com.kionavani.todotask.tasks

import com.kionavani.todotask.TelegramService
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class TelegramSenderTask @Inject constructor(
    private val service: TelegramService
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                runBlocking {
                    service.sendMessage("Build finished", token, chatId).apply {
                        println("Status = $status")
                    }
                }
                runBlocking {
                    service.upload(it, token, chatId).apply {
                        println("Status = $status")
                    }
                }
            }
    }
}