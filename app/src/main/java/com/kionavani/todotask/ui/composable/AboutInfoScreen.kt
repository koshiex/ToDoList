package com.kionavani.todotask.ui.composable

import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kionavani.todotask.ui.ViewFactoryInt
import com.kionavani.todotask.ui.theme.ToDoTaskTheme

@Composable
fun AboutInfoScreen(viewFactory: ViewFactoryInt) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(ToDoTaskTheme.colorScheme.backPrimary)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            factory = { viewFactory.provideView() ?: LinearLayout(it) }
        )
    }
}



