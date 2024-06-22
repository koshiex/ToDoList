package com.kionavani.todotask

import MainScreen
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.AddTaskScreen
import com.kionavani.todotask.ui.AddTaskScreenNav
import com.kionavani.todotask.ui.MainScreenNav
import com.kionavani.todotask.ui.theme.ToDoTaskTheme

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        val tasksRepository = TodoItemsRepository()
        val items = tasksRepository.getTodoItems()
        setContent {
            ToDoTaskTheme {
//                MainScreen(items = items)
//                AddTaskScreen()
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {

                    NavHost(
                        navController = navController,
                        startDestination = MainScreenNav
                    ) {
                        composable<MainScreenNav> {
                            MainScreen(items = items)
                        }
                        composable<AddTaskScreenNav> {
                            AddTaskScreen()
                        }
                    }
                }
            }
        }
    }
}



