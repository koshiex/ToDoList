package com.kionavani.todotask.domain

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.composable.AddTaskScreen
import com.kionavani.todotask.ui.composable.AddTaskScreenNav
import com.kionavani.todotask.ui.composable.MainScreen
import com.kionavani.todotask.ui.composable.MainScreenNav
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.TodoViewModelFactory

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { createViewModel() }
    private val provider by lazy { setupProvider() }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        provider.attachActivityContext(this)

        setContent {
            ToDoTaskTheme(dynamicColor = false) {
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
        }
    }

    override fun onDestroy() {
        provider.detachActivityContext()
        super.onDestroy()
    }

    private fun createViewModel() : TodoViewModel {
        val viewModelFactory = TodoViewModelFactory(TodoItemsRepository(), provider)
        return ViewModelProvider(this, viewModelFactory) [TodoViewModel::class.java]
    }
    private fun setupProvider() = (this.application as TodoApplication).resourcesProvider
}





