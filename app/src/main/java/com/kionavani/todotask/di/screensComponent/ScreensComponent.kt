package com.kionavani.todotask.di.screensComponent

import androidx.activity.ComponentActivity
import com.kionavani.todotask.di.ScreenScope
import com.kionavani.todotask.ui.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [ViewModelModule::class, ViewFactoryModule::class])
interface ScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activityContext(activityContext: ComponentActivity) : Builder
        fun build(): ScreenComponent
    }

    fun inject(mainActivity: MainActivity)
}