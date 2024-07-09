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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kionavani.todotask.R
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Главное активити приложения
 */
class MainActivity : ComponentActivity() {
    private lateinit var provider: ResourcesProvider
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var networkMonitor: NetworkMonitor

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

        networkMonitor.startMonitoring()
        setupWorker()
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
            networkMonitor.isConnected.collect { haveConnection ->
                if (haveConnection) {
                    addViewModel.dataChanged.collect { isChanged ->
                        mainViewModel.changeLoadingState()
                        if (!isChanged) mainViewModel.fetchData()
                    }
                } else {
                    mainViewModel.changeLoadingState()
                }
            }
        }
        lifecycleScope.launch {
            addViewModel.isErrorHappened.collect {
                if (it) mainViewModel.setUpdatingError()
            }
        }
    }

    private fun setupWorker() {
        val fetchWork = PeriodicWorkRequestBuilder<DataFetchWorker>(8, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DataFetchWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            fetchWork
        )

    }

    @Inject
    fun setProvider(provider: ResourcesProvider) {
        this.provider = provider
    }

    @Inject
    fun setMonitor(monitor: NetworkMonitor) {
        networkMonitor = monitor
    }

    @Inject
    fun setFactory(factory: ViewModelFactory) {
        viewModelFactory = factory
    }
}







