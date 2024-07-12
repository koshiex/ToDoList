package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.data.TasksMapper
import com.kionavani.todotask.data.TodoItemsRepositoryImpl
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.createHttpClient
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Composable
private fun createMainScreenViewModel(): MainScreenViewModel {
    val service = TasksServiceImpl(createHttpClient())
    return MainScreenViewModel(
        TodoItemsRepositoryImpl(
            service, TasksMapper(), CoroutineScope(SupervisorJob()), Dispatchers.IO
        ), ResourcesProvider(LocalContext.current)
    )
}

@Composable
private fun createAddTaskViewModel(): AddTaskViewModel {
    val service = TasksServiceImpl(createHttpClient())
    return AddTaskViewModel(
        TodoItemsRepositoryImpl(
            service, TasksMapper(), CoroutineScope(SupervisorJob()), Dispatchers.IO
        ), ResourcesProvider(LocalContext.current)
    )
}

@Preview(name = "Light Main Screen", showBackground = true)
@Composable
private fun PreviewLightMainScreen() {
    PreviewMainScreen(darkTheme = false)
}

@Preview(name = "Dark Main Screen", showBackground = true)
@Composable
private fun PreviewDarkMainScreen() {
    PreviewMainScreen(darkTheme = true)
}

@Composable
private fun PreviewMainScreen(darkTheme: Boolean) {
    val viewModel = createMainScreenViewModel()
    val addViewModel = createAddTaskViewModel()
    ToDoTaskTheme(darkTheme = darkTheme, dynamicColor = false) {
        PreviewNavHost(viewModel, addViewModel)
    }
}

@Preview(name = "Light Add Task Screen", showBackground = true)
@Composable
private fun PreviewLightAddTaskScreen() {
    PreviewAddTaskScreen(darkTheme = false)
}

@Preview(name = "Dark Add Task Screen", showBackground = true)
@Composable
private fun PreviewDarkAddTaskScreen() {
    PreviewAddTaskScreen(darkTheme = true)
}

@Composable
private fun PreviewAddTaskScreen(darkTheme: Boolean) {
    val viewModel = createAddTaskViewModel()
    ToDoTaskTheme(darkTheme = darkTheme, dynamicColor = false) {
        val navController = rememberNavController()
        val navigate = { navController.navigate(MainScreenNav) }
        AddTaskScreen(viewModel, "123", navigate)
    }
}

@Composable
private fun PreviewNavHost(viewModel: MainScreenViewModel, addViewModel: AddTaskViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = MainScreenNav
    ) {
        composable<MainScreenNav> {
            val navigate = { itemId: String? -> navController.navigate(AddTaskScreenNav(itemId)) }
            MainScreen(viewModel, navigate)
        }
        composable<AddTaskScreenNav> { backStackEntry ->
            val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
            val navigate = { navController.navigate(MainScreenNav) }
            AddTaskScreen(addViewModel, itemID, navigate)
        }
    }
}
