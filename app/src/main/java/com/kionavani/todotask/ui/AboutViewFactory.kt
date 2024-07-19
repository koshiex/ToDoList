package com.kionavani.todotask.ui

import android.content.Context
import androidx.activity.ComponentActivity
import com.yandex.div.core.BuildConfig
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.picasso.PicassoDivImageLoader
import javax.inject.Inject

class AboutViewFactory @Inject constructor(
    private val variableController: DivVariableController,
    private val activityContext: ComponentActivity
) : ViewFactoryInt {
    override fun provideView(): Div2View {
        val assetReader = AssetReader(activityContext)
        val divJson = assetReader.readJsonFromAssets("about_screen_div.json")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = activityContext,
            configuration = createDivConfiguration(activityContext),
            lifecycleOwner = activityContext
        )

        return Div2ViewFactory(divContext).createView(cardJson)
    }

    override fun createDivConfiguration(context: Context): DivConfiguration =
        DivConfiguration.Builder(PicassoDivImageLoader(context))
//        .actionHandler(SampleDivActionHandler())
            .divVariableController(variableController)
            .visualErrorsEnabled(BuildConfig.DEBUG)
            .build()

}