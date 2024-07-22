package com.kionavani.todotask.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.divkit.AboutViewFactory
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.data.Variable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DIVKIT_THEME_IS_DARK_VAR = "themeIsDark"

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
    @Inject
    lateinit var variableController: DivVariableController
    @Inject
    lateinit var aboutViewFactory: AboutViewFactory

    private val mainViewModel by viewModels<MainScreenViewModel> { viewModelFactory }
    private val addViewModel by viewModels<AddTaskViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }

    // TODO: сохранять в датастор
    private var currentThemeIsDark = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        (this.application as TodoApplication).appComponent.screenComponent().activityContext(this)
            .build().inject(this)

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

                val theme = Variable.BooleanVariable(DIVKIT_THEME_IS_DARK_VAR, currentThemeIsDark)
                variableController.putOrUpdate(theme)
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
                SetupUI(viewModelFactory, aboutViewFactory)
            }
        }
    }

    private fun isSystemThemeIsDark() = resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_NO
}







