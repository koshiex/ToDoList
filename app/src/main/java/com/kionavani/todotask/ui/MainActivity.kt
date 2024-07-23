package com.kionavani.todotask.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kionavani.todotask.data.DataStoreContract
import com.kionavani.todotask.ui.composable.SetupUI
import com.kionavani.todotask.ui.divkit.AboutViewFactory
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.data.Variable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private var currentThemeIsDark: Boolean? = null
    private val THEME_IS_DARK_KEY = booleanPreferencesKey(DataStoreContract.THEME_IS_DARK_KEY)

    @Inject
    lateinit var dataStoreInst: DataStore<Preferences>
    private val savedThemeFlow: Flow<Boolean?> by lazy {
        dataStoreInst.getNullableBoolean(
            THEME_IS_DARK_KEY
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (this.application as TodoApplication).appComponent.screenComponent().activityContext(this)
            .build().inject(this)

        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        lifecycleScope.launch {
            val savedTheme = savedThemeFlow.firstOrNull()
            currentThemeIsDark = savedTheme
            settingsViewModel.changeThemeBoolean(currentThemeIsDark)
            applyTheme()
        }


        networkMonitor.startMonitoring()
        provider.attachActivityContext(this)
        startCoroutines()
    }


    override fun onDestroy() {
        provider.detachActivityContext()
        networkMonitor.stopMonitoring()

        super.onDestroy()
    }

    private fun startCoroutines() {
        themeListener()

        lifecycleScope.launch {
            mainViewModel.fetchData()
        }

        lifecycleScope.launch {
            addViewModel.isErrorHappened.collect {
                if (it) mainViewModel.setUpdatingError()
            }
        }
    }

    private fun themeListener() {
        lifecycleScope.launch {
            settingsViewModel.selectedThemeState.collect {
                currentThemeIsDark = when (it) {
                    ThemeState.DARK -> true
                    ThemeState.LIGHT -> false
                    ThemeState.SYSTEM -> null
                }

                val theme = Variable.BooleanVariable(
                    DIVKIT_THEME_IS_DARK_VAR,
                    currentThemeIsDark ?: isSystemThemeIsDark()
                )
                variableController.putOrUpdate(theme)
                applyTheme()
                dataStoreInst.saveNullableBoolean(THEME_IS_DARK_KEY, currentThemeIsDark)
            }
        }
    }

    private fun applyTheme() {
        setContent {
            ToDoTaskTheme(darkTheme = currentThemeIsDark ?: isSystemThemeIsDark()) {
                SetupUI(this, aboutViewFactory)
            }
        }
    }

    private fun isSystemThemeIsDark() = resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_NO
}







