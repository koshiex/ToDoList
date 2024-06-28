package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.domain.LocalNavController
import com.kionavani.todotask.ui.viewmodels.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: TodoViewModel, itemID: String? = null) {
    val task by lazy { itemID?.let { viewModel.getTaskById(it) } }
    val deadlineSelectorText = stringResource(R.string.deadline_selector)

    var textFiledState by remember { mutableStateOf(task?.taskDescription ?: "") }
    var switchState by remember { mutableStateOf(false) }

    var dateTextState by remember { mutableStateOf(deadlineSelectorText) }
    var datePickerOnState by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    var deadlineDate by remember { mutableStateOf<Long?>(null) }

    var dropDownState by remember { mutableStateOf(false) }
    var selectedImportanceState by remember {
        mutableStateOf(task?.importance ?: Importance.REGULAR)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Header(
            viewModel,
            itemID,
            textFiledState,
            selectedImportanceState,
            deadlineDate
        )
        TaskTextField(textFiledState, onTextChange = { textFiledState = it })
        ImportanceDropDown(
            dropDownState,
            onDropDownStateChange = { dropDownState = it },
            onDropDownSelected = { selectedImportanceState = it },
            selectedImportanceState
        )
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        DeadlineRow(
            switchState = switchState,
            dateTextState = dateTextState,
            datePickerOnState = datePickerOnState,
            onSwitchStateChange = { switchState = it },
            onDateTextStateChange = {
                if (it != null) {
                    dateTextState = viewModel.dateToString(it)
                    deadlineDate = it
                } else {
                    dateTextState = deadlineSelectorText
                }
            },
            onDatePickerOnStateChange = { datePickerOnState = it },
            dateState = dateState
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        DeleteTaskRow(itemID) { itemID ->
            viewModel.deleteTodoItem(itemID)
        }
    }
}

@Composable
fun Header(
    viewModel: TodoViewModel,
    itemId: String?,
    descr: String,
    importance: Importance,
    deadlineDate: Long?
) {
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
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.save_task_button)).toUpperCase(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.inverseOnSurface
            ),
            modifier = Modifier.padding(top = 16.dp, end = 16.dp)
        ) {
            if (itemId != null) {
                val item = ToDoItem(
                    itemId,
                    descr,
                    false,
                    importance,
                    System.currentTimeMillis(),
                    deadlineDate,
                    System.currentTimeMillis()
                )
                viewModel.updateTodoItem(item)
            } else {
                val item = ToDoItem(
                    viewModel.getNextId(),
                    descr,
                    false,
                    importance,
                    System.currentTimeMillis(),
                    deadlineDate
                )
                viewModel.addTodoItem(item)
            }
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
            color = MaterialTheme.colorScheme.onPrimary
        ),
        placeholder = {
            Text(
                stringResource(R.string.text_field_hint),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                )
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        value = textFiledState,
        onValueChange = onTextChange,
    )
}

@Composable
fun ImportanceDropDown(
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit,
    onDropDownSelected: (Importance) -> Unit,
    selectedImportanceState: Importance
) {
    Box(modifier = Modifier.padding(start = 16.dp, top = 28.dp)) {
        Column {
            Text(
                text = stringResource(R.string.importance_drop_down),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Text(
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(top = 4.dp)
                    .clickable {
                        onDropDownStateChange(true)
                    },
                text = stringResource(selectedImportanceState.displayName),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                )
            )
        }

        DropdownMenu(
            offset = DpOffset(0.dp, (-20).dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
            expanded = dropDownState,
            onDismissRequest = { onDropDownStateChange(false) }
        ) {
            Importance.entries.forEach { importance ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(importance.displayName),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    onClick = {
                        onDropDownSelected(importance)
                        onDropDownStateChange(false)
                    }
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
    onDateTextStateChange: (Long?) -> Unit,
    onDatePickerOnStateChange: (Boolean) -> Unit,
    dateState: DatePickerState,
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
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            if (switchState) {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable {
                            onDatePickerOnStateChange(true)
                        },
                    text = dateTextState,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                )
            }

            if (datePickerOnState) {
                DatePickerDialog(
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    onDismissRequest = { onDatePickerOnStateChange(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            onDateTextStateChange(dateState.selectedDateMillis)
                            onDatePickerOnStateChange(false)
                        }) {
                            Text(
                                stringResource(android.R.string.ok),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onDatePickerOnStateChange(false) }) {
                            Text(
                                stringResource(android.R.string.cancel),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            )
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState,
                        colors = DatePickerDefaults.colors(
                            titleContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                            headlineContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                            weekdayContentColor = MaterialTheme.colorScheme.onTertiary,
                            subheadContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                            yearContentColor = MaterialTheme.colorScheme.onPrimary,
                            currentYearContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                            dayContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedDayContentColor = Color.White,
                            todayContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedDayContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            todayDateBorderColor = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                    )
                }
            }
        }

        Switch(
            checked = switchState,
            onCheckedChange = onSwitchStateChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.inverseSurface,
                checkedThumbColor = MaterialTheme.colorScheme.inverseOnSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}

@Composable
fun DeleteTaskRow(itemId: String?, delete: (String) -> Unit) {
    val navController = LocalNavController.current

    if (itemId != null) {
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
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = stringResource(R.string.delete_button),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        delete(itemId)
                        navController.navigate(MainScreenNav)
                    }
            )
        }
    }
}
