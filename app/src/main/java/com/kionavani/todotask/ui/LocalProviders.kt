package com.kionavani.todotask.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }
val LocalMainScreenViewModel = compositionLocalOf<MainScreenViewModel> { error("No TodoViewModel provided") }
