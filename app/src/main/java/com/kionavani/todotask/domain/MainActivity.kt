package com.kionavani.todotask.domain

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kionavani.todotask.R
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private lateinit var provider : ResourcesProvider
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel by viewModels<MainScreenViewModel> { viewModelFactory }
    private val addViewModel by viewModels<AddTaskViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        (this.application as TodoApplication).appComponent.inject(this)

        provider.attachActivityContext(this)
        startCoroutines()

        setContent {
            ToDoTaskTheme(dynamicColor = false) {
                SetupUI(viewModelFactory)
            }
        }
    }

    override fun onDestroy() {
        provider.detachActivityContext()
        super.onDestroy()
    }

    private fun startCoroutines() {
        lifecycleScope.launch {
            addViewModel.dataChanged.collect {isChanged ->
                if (!isChanged) mainViewModel.fetchData()
            }
        }
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







