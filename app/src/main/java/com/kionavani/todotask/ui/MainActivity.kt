package com.kionavani.todotask.ui

import MainScreen
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
import com.kionavani.todotask.ui.viewmodels.ToDoViewModel
import com.kionavani.todotask.ui.viewmodels.ToDoViewModelFactory
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.theme.ToDoTaskTheme

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ToDoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)
        
        val viewModelFactory = ToDoViewModelFactory(TodoItemsRepository())
        viewModel = ViewModelProvider(this, viewModelFactory) [ToDoViewModel::class.java]

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
}



