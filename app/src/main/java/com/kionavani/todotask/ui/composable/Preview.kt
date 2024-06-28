package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.domain.LocalNavController
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel

@Preview(name = "Light Main Screen", showBackground = true)
@Composable
fun PreviewLightMainScreen() {
    val viewModel = TodoViewModel(TodoItemsRepository(), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = false, dynamicColor = false) {
        PreviewNavHost(viewModel)
    }
}

@Preview(name = "Dark Main Screen", showBackground = true)
@Composable
fun PreviewDarkMainScreen() {
    val viewModel = TodoViewModel(TodoItemsRepository(), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = true, dynamicColor = false) {
        PreviewNavHost(viewModel)
    }
}

@Preview(name = "Light Add Task Screen", showBackground = true)
@Composable
fun PreviewLightAddTaskScreen() {
    val viewModel = TodoViewModel(TodoItemsRepository(), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = false, dynamicColor = false) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            AddTaskScreen(viewModel, "123")
        }
    }
}

@Preview(name = "Dark Add Task Screen", showBackground = true)
@Composable
fun PreviewDarkAddTaskScreen() {
    val viewModel = TodoViewModel(TodoItemsRepository(), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = true, dynamicColor = false) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            AddTaskScreen(viewModel, "123")
        }
    }
}


@Composable
fun PreviewNavHost(viewModel: TodoViewModel) {
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