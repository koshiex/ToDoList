package com.kionavani.todotask.domain

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.TodoViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository : TodoItemsRepository
    @Inject
    lateinit var provider : ResourcesProvider

    private val viewModelFactory by lazy { TodoViewModelFactory(repository, provider) }
    private val viewModel: TodoViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        (this.application as TodoApplication).appComponent.inject(this)

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
}







