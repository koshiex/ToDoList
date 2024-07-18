package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import kotlinx.serialization.Serializable

/**
 * Функция для создания навигации между экранами и прокидывания зависимостей
 */
@Composable
fun SetupUI(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreenNav
    ) {
        composable<MainScreenNav> {
            val viewModel = viewModel(
                modelClass = MainScreenViewModel::class.java,
                factory = viewModelFactory
            )

            val navigateToAdd = { itemId: String? -> navController.navigate(AddTaskScreenNav(itemId)) }
            val navigateToSettings = { navController.navigate(SettingsScreenNav) }

            MainScreen(viewModel, navigateToAdd)
        }
        composable<AddTaskScreenNav> { backStackEntry ->
            val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
            val viewModel = viewModel(
                modelClass = AddTaskViewModel::class.java,
                factory = viewModelFactory
            )
            val navigate = { navController.navigate(MainScreenNav) }

            AddTaskScreen(viewModel, itemID, navigate)
        }
        composable<SettingsScreenNav> {
            val viewModel = viewModel(
                modelClass = SettingsViewModel::class.java,
                factory = viewModelFactory
            )

            val navigate = { navController.navigate(MainScreenNav) }
            SettingsScreen(viewModel, navigate)
        }
    }

}

/**
 * Объект главного экрана для навигации
 */
@Serializable
object MainScreenNav
/**
 * Объект экрана редактирования/создания для навигации
 */
@Serializable
data class AddTaskScreenNav(val itemID: String?)

@Serializable
object SettingsScreenNav
