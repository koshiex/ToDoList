package com.kionavani.todotask.ui.divkit

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kionavani.todotask.ui.AssetReader
import com.yandex.div.core.BuildConfig
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.picasso.PicassoDivImageLoader
import javax.inject.Inject

private const val ABOUT_JSON_NAME = "about_screen_div.json"

class AboutViewFactory @Inject constructor(
    private val variableController: DivVariableController,
    private val activityContext: ComponentActivity
) : ViewFactoryInt {
    override lateinit var navController: NavHostController

    override fun provideView(): Div2View {
        val assetReader = AssetReader(activityContext)
        val divJson = assetReader.readJsonFromAssets(ABOUT_JSON_NAME)
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
        .actionHandler(AboutActionHandler(navController))
            .divVariableController(variableController)
            .visualErrorsEnabled(BuildConfig.DEBUG)
            .build()

}