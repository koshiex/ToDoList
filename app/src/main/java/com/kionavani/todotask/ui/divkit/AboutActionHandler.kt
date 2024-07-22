package com.kionavani.todotask.ui.divkit

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavHostController
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction


class AboutActionHandler(private val navController: NavHostController) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == ACTION_SCHEME && handleAction(url, view.view.context)) {
            true
        } else {
            return super.handleAction(action, view, resolver)
        }
    }

    private fun handleAction(action: Uri, context: Context) : Boolean {
        return when (action.host) {
            "github" -> {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(GITHUB_LINK)
                })
                true
            }
            "back" -> {
                navController.popBackStack()
                true
            }
            else -> false
        }
    }

    companion object {
        const val ACTION_SCHEME = "about-action"
        const val GITHUB_LINK = "https://github.com/koshiex/ToDoList"
    }

}