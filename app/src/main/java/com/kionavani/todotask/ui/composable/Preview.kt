package com.kionavani.todotask.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.NetworkMonitor
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private class MockTodoItemsRepository : TodoItemsRepository {
    override val todoItems: StateFlow<List<ToDoItem>> = MutableStateFlow(emptyList())
    override suspend fun fetchData() {}
    override suspend fun addTodoItem(item: ToDoItem) {}
    override suspend fun updateTodoItem(newItem: ToDoItem) {}
    override suspend fun deleteTodoItem(itemId: String) {}
    override suspend fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {}
    override suspend fun getTaskById(itemId: String): ToDoItem? = null
    override suspend fun getNextId(): String = "null"
    override suspend fun changeNetworkStatus(isOnline: Boolean) {}
}

@Composable
private fun createMainScreenViewModel(): MainScreenViewModel {
    return MainScreenViewModel(
        MockTodoItemsRepository(),
        ResourcesProvider(LocalContext.current),
        NetworkMonitor(LocalContext.current)
    )
}

@Composable
private fun createAddTaskViewModel(): AddTaskViewModel {
    return AddTaskViewModel(
        MockTodoItemsRepository(), ResourcesProvider(LocalContext.current)
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
    ToDoTaskTheme(darkTheme = darkTheme) {
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
    ToDoTaskTheme(darkTheme = darkTheme) {
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
