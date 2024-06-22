package com.kionavani.todotask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.Importance
import com.kionavani.todotask.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// TODO: Распилить на более мелкие функции
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen() {
    val deadlineSelectorText = stringResource(R.string.deadline_selector)

    var textFiledState by remember {
        mutableStateOf("")
    }
    var dropDownState by remember {
        mutableStateOf(false)
    }
    var switchState by remember {
        mutableStateOf(false)
    }
    var dateTextState by remember {
        mutableStateOf(deadlineSelectorText)
    }
    var datePickerOnState by remember {
        mutableStateOf(false)
    }

    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)



    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.close_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            ClickableText(
                text = AnnotatedString(
                    stringResource(id = R.string.save_task_button)
                ).toUpperCase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                // TODO
            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(1.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            placeholder = {
                Text(
                    stringResource(R.string.text_field_hint),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            },
            value = textFiledState,
            onValueChange = {
                textFiledState = it
            },
        )

        Box(modifier = Modifier.padding(start = 16.dp, top = 28.dp)) {
            Column {


                Text(
                    text = stringResource(R.string.importance_drop_down),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                ClickableText(
                    modifier = Modifier
                        .alpha(0.7f),
                    text = AnnotatedString(stringResource(Importance.LOW.displayName)),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    dropDownState = true
                }
            }


            DropdownMenu(
                offset = DpOffset(0.dp, (-20).dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                expanded = dropDownState,
                onDismissRequest = { dropDownState = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Importance.HIGH.displayName),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    },
                    onClick = { /*TODO*/ }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Importance.REGULAR.displayName),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    },
                    onClick = { /*TODO*/ }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Importance.LOW.displayName),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    },
                    onClick = { /*TODO*/ }
                )
            }
        }

        Divider(
            modifier = Modifier
                .alpha(0.1f)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline
        )

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier.height(50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.switch_deadline_descr),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                if (switchState) {
                    ClickableText(
                        text = AnnotatedString(dateTextState),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        datePickerOnState = true
                    }
                }

                if (datePickerOnState) {
                    DatePickerDialog(
                        onDismissRequest = { datePickerOnState = false },
                        confirmButton = {
                            TextButton(onClick = {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                dateTextState = dateState.selectedDateMillis?.let {
                                    dateFormat.format(Date(it))
                                } ?: deadlineSelectorText
                                datePickerOnState = false
                            }) {
                                Text(stringResource(android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { datePickerOnState = false }) {
                                Text(stringResource(android.R.string.cancel))
                            }
                        }
                    ) {
                        DatePicker(state = dateState)
                    }
                }

            }

            Switch(
                checked = switchState,
                onCheckedChange = {
                    switchState = !switchState
                }
            )
        }

        Divider(
            modifier = Modifier
                .alpha(0.1f)
                .padding(top = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.delete_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            ClickableText(
                modifier = Modifier.padding(start = 12.dp),
                text = AnnotatedString(stringResource(R.string.delete_button)),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            ) {
                // TODO:
            }
        }
    }
}