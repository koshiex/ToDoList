package com.kionavani.todotask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ViewModelModule {
    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    fun bindMainViewModel(viewModel: MainScreenViewModel): ViewModel
    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun bindAddTaskViewModel(viewModel: AddTaskViewModel) : ViewModel
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}