package com.kionavani.todotask.di

import androidx.lifecycle.ViewModelProvider
import com.kionavani.todotask.ui.viewmodels.TodoViewModel
import com.kionavani.todotask.ui.viewmodels.TodoViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

//@Module
//interface ViewModelModule {
//    @Binds
//    fun bindViewModelFactory(factory: TodoViewModelFactory): ViewModelProvider.Factory
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MyViewModel::class)
//    fun bindMyViewModel(viewModel: TodoViewModel): TodoViewModel
//}