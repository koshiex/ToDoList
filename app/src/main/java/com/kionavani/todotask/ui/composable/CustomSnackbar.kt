package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
    actionColor: Color = MaterialTheme.colorScheme.primary
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        actionContentColor = actionColor,
        containerColor = containerColor
    ) {
        Column {
            Text(
                text = snackbarData.visuals.message,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                Text(
                    text = actionLabel,
                    color = actionColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable {
                        snackbarData.performAction()
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun showSnackBar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration,
    onActionMessage: String? = null,
    onClick: (() -> Unit?)? = null
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = onActionMessage,
                duration = duration
            )
        when (result) {
            SnackbarResult.ActionPerformed ->
                if (onClick != null) {
                    onClick()
                }

            SnackbarResult.Dismissed -> Unit
        }
    }
}
