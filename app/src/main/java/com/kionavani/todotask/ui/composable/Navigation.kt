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
import kotlinx.serialization.Serializable

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

            val navigate = { itemId: String? -> navController.navigate(AddTaskScreenNav(itemId)) }

            MainScreen(viewModel, navigate)
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
    }

}

@Serializable
object MainScreenNav

@Serializable
data class AddTaskScreenNav(val itemID: String?)
