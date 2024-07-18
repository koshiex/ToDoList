package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.kionavani.todotask.R
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigate: () -> Unit
) {
    Scaffold(
        topBar = { SettingsTopBar(navigate) }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Text(text = "test")
        }
    }
}

@Composable
fun SettingsTopBar(
    navigate: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = navigate) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_back_icon),
                contentDescription = null,
                tint = ToDoTaskTheme.colorScheme.labelPrimary
            )
        }

        Text(
            text = stringResource(R.string.settings).toUpperCase(),
            color = ToDoTaskTheme.colorScheme.colorBlue,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

