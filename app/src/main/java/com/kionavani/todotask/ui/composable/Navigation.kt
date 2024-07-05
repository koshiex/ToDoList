package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.LocalNavController
import com.kionavani.todotask.ui.LocalMainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.serialization.Serializable

// TODO: прокидывать лямбду вместе контроллера
@Composable
fun SetupUI(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = MainScreenNav
            ) {
                composable<MainScreenNav> {
                    val viewModel = viewModel(
                        modelClass = MainScreenViewModel::class.java,
                        factory = viewModelFactory
                    )

                    MainScreen(viewModel)
                }
                composable<AddTaskScreenNav> { backStackEntry ->
                    val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
                    val viewModel = viewModel(
                        modelClass = AddTaskViewModel::class.java,
                        factory = viewModelFactory
                    )
                    AddTaskScreen(viewModel, itemID)
                }
            }
    }
}

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
