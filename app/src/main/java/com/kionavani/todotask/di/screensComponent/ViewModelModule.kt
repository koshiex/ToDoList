package com.kionavani.todotask.di.screensComponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kionavani.todotask.di.ScreenScope
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Модуль для инжектирования ViewModel-ей
 */
@Module
interface ViewModelModule {
    @Binds
    @ScreenScope
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    fun bindMainViewModel(viewModel: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun bindAddTaskViewModel(viewModel: AddTaskViewModel) : ViewModel

    @Binds
    @ScreenScope
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel) : ViewModel

    @Binds
    @ScreenScope
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}