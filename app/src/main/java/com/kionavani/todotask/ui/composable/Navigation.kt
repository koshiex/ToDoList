package com.kionavani.todotask.ui.composable

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kionavani.todotask.ui.divkit.ViewFactoryInt
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import kotlinx.serialization.Serializable

/**
 * Функция для создания навигации между экранами и прокидывания зависимостей
 */
@Composable
fun SetupUI(
    storeOwner: ViewModelStoreOwner, aboutViewFactory: ViewFactoryInt
) {
    val navController = rememberNavController()
    aboutViewFactory.navController = navController

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ToDoTaskTheme.colorScheme.backPrimary)
            .safeDrawingPadding()
    ) {
        NavHost(
            navController = navController, startDestination = MainScreenNav
        ) {
            composable<MainScreenNav>(
                enterTransition = { enterTransition() },
                exitTransition = { exitTransition() },
                popEnterTransition = { enterTransition() },
                popExitTransition = { exitTransition() }
            ) {
                val viewModel = viewModel<MainScreenViewModel>(storeOwner)

                val navigateToAdd =
                    { itemId: String? -> navController.navigate(AddTaskScreenNav(itemId)) }
                val navigateToSettings = { navController.navigate(SettingsScreenNav) }

                MainScreen(viewModel, navigateToAdd, navigateToSettings)
            }
            composable<AddTaskScreenNav>(
                enterTransition = { enterTransition() },
                exitTransition = { exitTransition() },
                popEnterTransition = { enterTransition() },
                popExitTransition = { exitTransition() }
            ) { backStackEntry ->
                val itemID = backStackEntry.toRoute<AddTaskScreenNav>().itemID
                val viewModel = viewModel<AddTaskViewModel>(storeOwner)

                val navigate = { navController.popBackStack() }
                Log.i("VIEWMODEL", "Hash in compose - ${viewModel.hashCode()}")

                AddTaskScreen(viewModel, itemID, navigate)
            }
            composable<SettingsScreenNav>(
                enterTransition = { enterTransition() },
                exitTransition = { exitTransition() },
                popEnterTransition = { enterTransition() },
                popExitTransition = { exitTransition() }
            ) {
                val viewModel = viewModel<SettingsViewModel>(storeOwner)

                val navigateBack = { navController.popBackStack() }
                val navigateToInfo = { navController.navigate(AboutInfoNav) }
                SettingsScreen(viewModel, navigateBack, navigateToInfo)
            }
            composable<AboutInfoNav>(
                enterTransition = { enterTransition() },
                exitTransition = { exitTransition() },
                popEnterTransition = { enterTransition() },
                popExitTransition = { exitTransition() }
            ) {
                AboutInfoScreen(aboutViewFactory)
            }
        }
    }
}

fun enterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { 1000 }, animationSpec = tween(700)
    ) + fadeIn(animationSpec = tween(700))
}

fun exitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { -1000 }, animationSpec = tween(700)
    ) + fadeOut(animationSpec = tween(700))
}

/**
 * Объект главного экрана для навигации
 */
@Serializable
object MainScreenNav

/**
 * Объект экрана редактирования/создания для навигации
 */
@Serializable
data class AddTaskScreenNav(val itemID: String?)

@Serializable
object SettingsScreenNav

@Serializable
object AboutInfoNav
