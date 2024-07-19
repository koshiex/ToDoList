package com.kionavani.todotask.ui.composable

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kionavani.todotask.ui.AssetReader
import com.kionavani.todotask.ui.Div2ViewFactory
import com.kionavani.todotask.ui.MainActivity
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.picasso.PicassoDivImageLoader

@Composable
fun AboutInfoScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        factory =
        { context ->
            provideAboutScreenView(context, lifecycleOwner)
        })
}

private fun provideAboutScreenView(context: Context, lifecycleOwner: LifecycleOwner): Div2View {
    val assetReader = AssetReader(context)
    val divJson = assetReader.readJsonFromAssets("about_screen_div.json")
    val cardJson = divJson.getJSONObject("card")

    val divContext = Div2Context(
        baseContext = context as ContextThemeWrapper,
        configuration = createDivConfiguration(context),
        lifecycleOwner = lifecycleOwner
    )

    return Div2ViewFactory(divContext).createView(cardJson)
}

private fun createDivConfiguration(context: Context): DivConfiguration {
    val variableController = DivVariableController()
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
//        .actionHandler(SampleDivActionHandler())
        .divVariableController(variableController)
        .build()
}

