package com.kionavani.todotask.domain

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.kionavani.todotask.R
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private lateinit var provider : ResourcesProvider
    private lateinit var viewModelFactory: ViewModelProvider.Factory

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
            viewModel.errorFlow.flowWithLifecycle(lifecycle).collect { error ->
                val errorMessage = error?.message ?: provider.getString(R.string.smth_error)
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        provider.detachActivityContext()
        super.onDestroy()
    }

    @Inject
    fun setProvider(provider: ResourcesProvider) {
        this.provider = provider
    }

    @Inject
    fun setFactory(factory: ViewModelFactory) {
        viewModelFactory = factory
    }
}







