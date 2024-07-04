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
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.createHttpClient
import com.kionavani.todotask.ui.LocalNavController
import com.kionavani.todotask.ui.LocalTodoViewModel
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel

@Preview(name = "Light Main Screen", showBackground = true)
@Composable
private fun PreviewLightMainScreen() {
    val service = TasksServiceImpl(createHttpClient())
    val viewModel =
        TodoViewModel(TodoItemsRepository(service), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = false, dynamicColor = false) {
        PreviewNavHost(viewModel)
    }
}

@Preview(name = "Dark Main Screen", showBackground = true)
@Composable
private fun PreviewDarkMainScreen() {
    val service = TasksServiceImpl(createHttpClient())
    val viewModel =
        TodoViewModel(TodoItemsRepository(service), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = true, dynamicColor = false) {
        PreviewNavHost(viewModel)
    }
}

@Preview(name = "Light Add Task Screen", showBackground = true)
@Composable
private fun PreviewLightAddTaskScreen() {
    val service = TasksServiceImpl(createHttpClient())
    val viewModel =
        TodoViewModel(TodoItemsRepository(service), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = false, dynamicColor = false) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            CompositionLocalProvider(LocalTodoViewModel provides viewModel) {
                AddTaskScreen("123")
            }
        }
    }
}

@Preview(name = "Dark Add Task Screen", showBackground = true)
@Composable
private fun PreviewDarkAddTaskScreen() {
    val service = TasksServiceImpl(createHttpClient())
    val viewModel =
        TodoViewModel(TodoItemsRepository(service), ResourcesProvider(LocalContext.current))
    ToDoTaskTheme(darkTheme = true, dynamicColor = false) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            CompositionLocalProvider(LocalTodoViewModel provides viewModel) {
                AddTaskScreen("123")
            }
        }
    }
}


@Composable
private fun PreviewNavHost(viewModel: TodoViewModel) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        CompositionLocalProvider(LocalTodoViewModel provides viewModel) {
            NavHost(
                navController = navController, startDestination = MainScreenNav
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