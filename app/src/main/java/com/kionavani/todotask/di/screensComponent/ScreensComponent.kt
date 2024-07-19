package com.kionavani.todotask.di.screensComponent

import com.kionavani.todotask.di.ScreenScope
import com.kionavani.todotask.ui.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [ViewModelModule::class])
interface ScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ScreenComponent
    }

    fun inject(mainActivity: MainActivity)
}