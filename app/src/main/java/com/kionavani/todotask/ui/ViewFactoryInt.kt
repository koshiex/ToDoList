package com.kionavani.todotask.ui

import android.content.Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View

interface ViewFactoryInt {
    fun provideView(): Div2View?
    fun createDivConfiguration(context: Context): DivConfiguration?
}