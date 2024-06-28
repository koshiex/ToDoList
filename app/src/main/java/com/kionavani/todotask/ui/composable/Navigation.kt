package com.kionavani.todotask.ui.composable

import kotlinx.serialization.Serializable

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
