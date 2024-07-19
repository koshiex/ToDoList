package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kionavani.todotask.ui.ViewFactoryInt
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import com.yandex.div.core.view2.Div2View
import kotlinx.serialization.Serializable

/**
 * Функция для создания навигации между экранами и прокидывания зависимостей
 */
@Composable
fun SetupUI(viewModelFactory: ViewModelProvider.Factory, aboutViewFactory: ViewFactoryInt) {
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

            MainScreen(viewModel, navigateToAdd, navigateToSettings)
        }
        composable<AddTaskScreenNav> { backStackEntry ->
            val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
            val viewModel = viewModel(
                modelClass = AddTaskViewModel::class.java,
                factory = viewModelFactory
            )
            val navigate = { navController.popBackStack() }

            AddTaskScreen(viewModel, itemID, navigate)
        }
        composable<SettingsScreenNav> {
            val viewModel = viewModel(
                modelClass = SettingsViewModel::class.java,
                factory = viewModelFactory
            )

            val navigateBack = { navController.popBackStack() }
            val navigateToInfo = { navController.navigate(AboutInfoNav) }
            SettingsScreen(viewModel, navigateBack, navigateToInfo)
        }
        composable<AboutInfoNav> {
            AboutInfoScreen(aboutViewFactory)
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

@Serializable
object AboutInfoNav
