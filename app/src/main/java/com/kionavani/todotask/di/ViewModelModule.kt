package com.kionavani.todotask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TodoViewModel::class)
    fun bindTodoViewModel(viewModel: TodoViewModel): ViewModel
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}