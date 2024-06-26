package com.kionavani.todotask.ui

import com.kionavani.todotask.ToDoItem
import kotlinx.serialization.Serializable

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
