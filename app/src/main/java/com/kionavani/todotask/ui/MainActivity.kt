package com.kionavani.todotask.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
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
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private val insetsController by lazy { setupInsetsController() }
    // TODO: сохранять в датастор
    private var currentThemeIsDark = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        (this.application as TodoApplication).screenComponent.inject(this)

        networkMonitor.startMonitoring()
        provider.attachActivityContext(this)
        startCoroutines()

        applyTheme()
    }


    override fun onDestroy() {
        provider.detachActivityContext()
        networkMonitor.stopMonitoring()
        super.onDestroy()
    }

    private fun startCoroutines() {
        lifecycleScope.launch {
            settingsViewModel.selectedThemeState.collectLatest {
                currentThemeIsDark = when(it) {
                    ThemeState.DARK -> true
                    ThemeState.LIGHT -> false
                    ThemeState.SYSTEM -> isSystemThemeIsDark()
                }

                applyTheme()
            }
        }

        lifecycleScope.launch {
            mainViewModel.fetchData()
        }

        lifecycleScope.launch {
            addViewModel.isErrorHappened.collect {
                if (it) mainViewModel.setUpdatingError()
            }
        }
    }

    private fun applyTheme() {
        setContent {
            ToDoTaskTheme(darkTheme = currentThemeIsDark) {
                SetupUI(viewModelFactory)
            }
        }
    }

    private fun isSystemThemeIsDark() = resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_NO

    private fun setupInsetsController() = WindowInsetsControllerCompat(window, window.decorView)

}







