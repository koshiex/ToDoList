package com.kionavani.todotask.domain

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.TodoViewModelFactory
import kotlinx.coroutines.launch

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    private val repository by lazy { setupRepository() }
    private val provider by lazy { setupProvider() }
    private val viewModelFactory by lazy { TodoViewModelFactory(repository, provider) }
    private val viewModel: TodoViewModel by viewModels { viewModelFactory }

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
                SetupUI(viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.errorFlow.collect { errorMessage ->
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
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
    private fun setupRepository() = (this.application as TodoApplication).todoItemsRepository
}







