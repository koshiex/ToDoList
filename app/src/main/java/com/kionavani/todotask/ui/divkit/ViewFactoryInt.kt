package com.kionavani.todotask.ui.divkit

import android.content.Context
import androidx.navigation.NavHostController
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View

interface ViewFactoryInt {
    var navController: NavHostController
    fun provideView(): Div2View?
    fun createDivConfiguration(context: Context): DivConfiguration?
}