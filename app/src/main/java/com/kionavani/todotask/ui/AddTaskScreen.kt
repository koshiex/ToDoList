package com.kionavani.todotask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavController
import com.kionavani.todotask.Importance
import com.kionavani.todotask.LocalNavController
import com.kionavani.todotask.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen() {
    val deadlineSelectorText = stringResource(R.string.deadline_selector)

    var textFiledState by remember { mutableStateOf("") }
    var dropDownState by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }
    var dateTextState by remember { mutableStateOf(deadlineSelectorText) }
    var datePickerOnState by remember { mutableStateOf(false) }

    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Header()
        TaskTextField(textFiledState, onTextChange = { textFiledState = it })
        ImportanceDropDown(dropDownState, onDropDownStateChange = { dropDownState = it })
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        DeadlineRow(
            switchState = switchState,
            dateTextState = dateTextState,
            datePickerOnState = datePickerOnState,
            onSwitchStateChange = { switchState = it },
            onDateTextStateChange = { dateTextState = it },
            onDatePickerOnStateChange = { datePickerOnState = it },
            dateState = dateState,
            deadlineSelectorText = deadlineSelectorText
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        DeleteTaskRow()
    }
}

@Composable
fun Header() {
    val navController = LocalNavController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            onClick = { navController.navigate(MainScreenNav) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.close_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.save_task_button)).toUpperCase(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(top = 16.dp, end = 16.dp)
        ) {
            navController.navigate(MainScreenNav)
        }
    }
}

@Composable
fun TaskTextField(textFiledState: String, onTextChange: (String) -> Unit) {
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
        onValueChange = onTextChange,
    )
}

@Composable
fun ImportanceDropDown(
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit
) {
    Box(modifier = Modifier.padding(start = 16.dp, top = 28.dp)) {
        Column {
            Text(
                text = stringResource(R.string.importance_drop_down),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )

            ClickableText(
                modifier = Modifier.alpha(0.7f),
                text = AnnotatedString(stringResource(Importance.LOW.displayName)),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                onDropDownStateChange(true)
            }
        }

        DropdownMenu(
            offset = DpOffset(0.dp, (-20).dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
            expanded = dropDownState,
            onDismissRequest = { onDropDownStateChange(false) }
        ) {
            Importance.entries.forEach { importance ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(importance.displayName),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    },
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineRow(
    switchState: Boolean,
    dateTextState: String,
    datePickerOnState: Boolean,
    onSwitchStateChange: (Boolean) -> Unit,
    onDateTextStateChange: (String) -> Unit,
    onDatePickerOnStateChange: (Boolean) -> Unit,
    dateState: DatePickerState,
    deadlineSelectorText: String
) {
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
                    onDatePickerOnStateChange(true)
                }
            }

            if (datePickerOnState) {
                DatePickerDialog(
                    onDismissRequest = { onDatePickerOnStateChange(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = dateState.selectedDateMillis?.let {
                                dateFormat.format(Date(it))
                            } ?: deadlineSelectorText
                            onDateTextStateChange(formattedDate)
                            onDatePickerOnStateChange(false)
                        }) {
                            Text(stringResource(android.R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onDatePickerOnStateChange(false) }) {
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
            onCheckedChange = onSwitchStateChange
        )
    }
}

@Composable
fun DeleteTaskRow() {
    val navController = LocalNavController.current

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
            navController.navigate(MainScreenNav)
        }
    }
}
