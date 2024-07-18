package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kionavani.todotask.R
import com.kionavani.todotask.ui.ThemeState
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel, navigate: () -> Unit
) {
    val themeState by viewModel.selectedThemeState.collectAsStateWithLifecycle()
    var dropDownState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SettingsTopBar(navigate) }) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            ThemeSelector(
                selectedThemeState = themeState,
                dropDownState = dropDownState,
                onDropDownStateChange = { dropDownState = it },
                onDropDownSelected = viewModel::changeTheme
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp))
        }
    }
}

@Composable
fun ThemeSelector(
    selectedThemeState: ThemeState,
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit,
    onDropDownSelected: (ThemeState) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onDropDownStateChange(true) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.theme_selector),
            color = ToDoTaskTheme.colorScheme.labelPrimary,
            style = MaterialTheme.typography.bodyMedium
        )
        Box {
            Text(
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(top = 4.dp),
                text = stringResource(selectedThemeState.displayName),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = ToDoTaskTheme.colorScheme.colorBlue
                )
            )

            DropdownMenu(
                offset = DpOffset(0.dp, (-20).dp),
                modifier = Modifier.background(ToDoTaskTheme.colorScheme.backSecondary),
                expanded = dropDownState,
                onDismissRequest = { onDropDownStateChange(false) }) {
                ThemeState.entries.forEach { theme ->
                    DropdownMenuItem(text = {
                        Text(
                            text = stringResource(theme.displayName),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = ToDoTaskTheme.colorScheme.labelPrimary
                            )
                        )
                    }, onClick = {
                        onDropDownSelected(theme)
                        onDropDownStateChange(false)
                    })
                }
            }
        }
    }
}


@Composable
fun SettingsTopBar(
    navigate: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(3.dp)
            .fillMaxWidth()
            .background(ToDoTaskTheme.colorScheme.backSecondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(modifier = Modifier.statusBarsPadding(),
            onClick = navigate) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_back_icon),
                contentDescription = null,
                tint = ToDoTaskTheme.colorScheme.labelPrimary
            )
        }

        Text(
            modifier = Modifier.statusBarsPadding(),
            text = stringResource(R.string.settings).toUpperCase(),
            color = ToDoTaskTheme.colorScheme.colorBlue,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

