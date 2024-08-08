package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Кастомный снек-бак
 */

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    containerColor: Color = ToDoTaskTheme.colorScheme.colorRed,
    contentColor: Color = ToDoTaskTheme.colorScheme.colorWhite,
    actionColor: Color = ToDoTaskTheme.colorScheme.backPrimary,
    dismissMessage: String = "",
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        actionContentColor = actionColor,
        containerColor = containerColor,
        dismissActionContentColor = actionColor,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = snackbarData.visuals.message,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
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

                if (snackbarData.visuals.withDismissAction) {
                    Text(
                        text = dismissMessage,
                        color = actionColor,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.clickable {
                            snackbarData.dismiss()
                        }
                    )
                }
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
    onActionClick: (() -> Unit?)? = null,
    onDismissClick: (() -> Unit?)? = null,
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = onActionMessage,
                duration = duration,
                withDismissAction = onDismissClick != null
            )
        when (result) {
            SnackbarResult.ActionPerformed ->
                if (onActionClick != null) {
                    onActionClick()
                }

            SnackbarResult.Dismissed ->
                if (onDismissClick != null) {
                    onDismissClick()
                }
        }
    }
}
