package com.kionavani.todotask.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import com.kionavani.todotask.ui.viewmodels.TodoViewModel

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }
val LocalTodoViewModel = compositionLocalOf<TodoViewModel> { error("No TodoViewModel provided") }
