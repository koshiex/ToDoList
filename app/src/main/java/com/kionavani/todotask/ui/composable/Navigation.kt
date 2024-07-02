package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.domain.LocalNavController
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import kotlinx.serialization.Serializable

@Composable
fun SetupUI(viewModel: TodoViewModel) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = MainScreenNav
        ) {
            composable<MainScreenNav> {
                MainScreen(viewModel)
            }
            composable<AddTaskScreenNav> { backStackEntry ->
                val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
                AddTaskScreen(viewModel, itemID)
            }
        }
    }
}

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
