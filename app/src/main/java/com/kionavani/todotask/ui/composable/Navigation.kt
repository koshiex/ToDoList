package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.LocalNavController
import com.kionavani.todotask.ui.LocalMainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.serialization.Serializable

@Composable
fun SetupUI(viewModel: MainScreenViewModel) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        CompositionLocalProvider(LocalMainScreenViewModel provides viewModel) {
            NavHost(
                navController = navController,
                startDestination = MainScreenNav
            ) {
                composable<MainScreenNav> {
                    MainScreen()
                }
                composable<AddTaskScreenNav> { backStackEntry ->
                    val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
                    AddTaskScreen(itemID)
                }
            }
        }
    }
}

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
