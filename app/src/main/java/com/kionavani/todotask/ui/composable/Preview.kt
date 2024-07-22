package com.kionavani.todotask.ui.composable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.NetworkMonitor
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.divkit.ViewFactoryInt
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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

@Composable
private fun createSettingsViewModel(): SettingsViewModel {
    return SettingsViewModel()
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
    val settingsViewModel = createSettingsViewModel()
    ToDoTaskTheme(darkTheme = darkTheme) {
        PreviewNavHost(viewModel, addViewModel, settingsViewModel)
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

@Preview(name = "Dark Settings Screen", showBackground = true)
@Composable
private fun PreviewDarkSettingsScreen() {
    PreviewSettingsScreen(darkTheme = true)
}

@Preview(name = "Light Settings Screen", showBackground = true)
@Composable
private fun PreviewLightSettingsScreen() {
    PreviewSettingsScreen(darkTheme = false)
}

@Composable
private fun PreviewAddTaskScreen(darkTheme: Boolean) {
    val viewModel = createAddTaskViewModel()
    ToDoTaskTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()
        val navigate = { navController.popBackStack() }
        AddTaskScreen(viewModel, "123", navigate)
    }
}

@Composable
private fun PreviewSettingsScreen(darkTheme: Boolean) {
    val viewModel = createSettingsViewModel()
    ToDoTaskTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()
        val navigate = { navController.popBackStack() }
        val navigateToInfo = { navController.navigate(AboutInfoNav) }
        SettingsScreen(viewModel, navigate, navigateToInfo)
    }
}

@Composable
private fun PreviewNavHost(viewModel: MainScreenViewModel, addViewModel: AddTaskViewModel, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = MainScreenNav
    ) {
        composable<MainScreenNav> {
            val navigate = { itemId: String? -> navController.navigate(AddTaskScreenNav(itemId)) }
            val navigateToSettings = { navController.navigate(SettingsScreenNav) }
            MainScreen(viewModel, navigate, navigateToSettings)
        }
        composable<AddTaskScreenNav> { backStackEntry ->
            val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
            val navigate = { navController.popBackStack() }
            AddTaskScreen(addViewModel, itemID, navigate)
        }
        composable<SettingsScreenNav> {
            val navigate = { navController.popBackStack() }
            val navigateToInfo = { navController.navigate(AboutInfoNav) }
            SettingsScreen(settingsViewModel, navigate, navigateToInfo)
        }
        composable<AboutInfoNav> {
            AboutInfoScreen(MockViewFactory(navController))
        }
    }
}


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

private class MockViewFactory(override var navController: NavHostController) : ViewFactoryInt {
    override fun provideView(): Div2View? = null
    override fun createDivConfiguration(context: Context): DivConfiguration? = null
}
