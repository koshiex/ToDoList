package com.kionavani.todotask.di.screensComponent

import com.kionavani.todotask.di.ScreenScope
import com.kionavani.todotask.ui.AboutViewFactory
import com.yandex.div.core.expression.variables.DivVariableController
import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides

@Module
class ViewFactoryModule {
    @Provides
    @ScreenScope
    fun provideAboutViewFactory(
        variableController: DivVariableController,
        activityContext: ComponentActivity
    ): AboutViewFactory = AboutViewFactory(variableController, activityContext)

    @Provides
    @ScreenScope
    fun provideVariableController(): DivVariableController = DivVariableController()
}