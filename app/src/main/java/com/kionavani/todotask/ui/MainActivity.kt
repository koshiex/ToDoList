package com.kionavani.todotask.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Главное активити приложения
 */
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var provider: ResourcesProvider
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val mainViewModel by viewModels<MainScreenViewModel> { viewModelFactory }
    private val addViewModel by viewModels<AddTaskViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        (this.application as TodoApplication).screenComponent.inject(this)

        networkMonitor.startMonitoring()
        provider.attachActivityContext(this)
        startCoroutines()

        setContent {
            ToDoTaskTheme {
                SetupUI(viewModelFactory)
            }
        }
    }

    override fun onDestroy() {
        provider.detachActivityContext()
        networkMonitor.stopMonitoring()
        super.onDestroy()
    }

    private fun startCoroutines() {
        lifecycleScope.launch {
            mainViewModel.fetchData()
        }

        lifecycleScope.launch {
            addViewModel.isErrorHappened.collect {
                if (it) mainViewModel.setUpdatingError()
            }
        }
    }
}







