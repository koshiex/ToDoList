package com.kionavani.todotask.di.screensComponent

import com.kionavani.todotask.di.ScreenScope
import com.kionavani.todotask.ui.MainActivity
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [ViewModelModule::class])
interface ScreenComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }

    fun inject(mainActivity: MainActivity)
}