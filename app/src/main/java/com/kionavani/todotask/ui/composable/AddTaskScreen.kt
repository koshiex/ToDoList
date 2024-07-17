package com.kionavani.todotask.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.AddTaskViewModel

/**
 * UI экрана редактирования/создания новой таски
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: AddTaskViewModel, itemID: String? = null, navigate: () -> Unit) {
    LaunchedEffect(itemID) {
        Log.i("ADD_SCREEN", "Task id: $itemID")
        if (itemID != null) {
            viewModel.loadTask(itemID)
        } else {
            viewModel.initializeState(null)
        }
    }

    val textFiledState by viewModel.textFieldState.collectAsState()
    val switchState by viewModel.switchState.collectAsState()
    val dateTextState by viewModel.dateTextState.collectAsState()
    val datePickerOnState by viewModel.datePickerOnState.collectAsState()
    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val dropDownState by viewModel.dropDownState.collectAsState()
    val selectedImportanceState by viewModel.selectedImportanceState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(ToDoTaskTheme.colorScheme.backPrimary)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Header(viewModel, itemID, navigate)
        TaskTextField(textFiledState, onTextChange = viewModel::onTextChange)
        ImportanceDropDown(
            dropDownState,
            onDropDownStateChange = viewModel::onDropDownStateChange,
            onDropDownSelected = viewModel::onDropDownSelected,
            selectedImportanceState
        )
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        DeadlineRow(
            switchState = switchState,
            dateTextState = dateTextState,
            datePickerOnState = datePickerOnState,
            onSwitchStateChange = viewModel::onSwitchStateChange,
            onDateTextStateChange = viewModel::onDateTextStateChange,
            onDatePickerOnStateChange = viewModel::onDatePickerOnStateChange,
            dateState = dateState
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        DeleteTaskRow(
            itemID, delete = { itemID ->
                viewModel.deleteTodoItem(itemID)
            }, navigate
        )
    }
}

@Composable
fun Header(
    viewModel: AddTaskViewModel, itemId: String?, navigate: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderIconButton(navigate)
        HeaderTextButton(viewModel, itemId, navigate)
    }
}

@Composable
fun HeaderIconButton(navigate: () -> Unit) {
    IconButton(modifier = Modifier.padding(top = 16.dp, start = 16.dp), onClick = { navigate() }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.close_icon),
            contentDescription = null,
            tint = ToDoTaskTheme.colorScheme.labelPrimary
        )
    }
}

@Composable
fun HeaderTextButton(viewModel: AddTaskViewModel, itemId: String?, navigate: () -> Unit) {
    Text(text = stringResource(id = R.string.save_task_button).uppercase(),
        style = MaterialTheme.typography.labelMedium.copy(
            color = ToDoTaskTheme.colorScheme.colorBlue
        ),
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp)
            .clickable {
                viewModel.addOrUpdateTask(itemId)
                navigate()
            })
}

@Composable
fun TaskTextField(textFiledState: String, onTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .background(ToDoTaskTheme.colorScheme.backSecondary, RoundedCornerShape(8.dp))
    ) {
        BasicTextField(
            value = textFiledState,
            onValueChange = onTextChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = ToDoTaskTheme.colorScheme.labelPrimary
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            decorationBox = { innerTextField ->
                if (textFiledState.isEmpty()) {
                    Text(
                        text = stringResource(R.string.text_field_hint),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ToDoTaskTheme.colorScheme.labelTertiary
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}


@Composable
fun ImportanceDropDown(
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit,
    onDropDownSelected: (Importance) -> Unit,
    selectedImportanceState: Importance
) {
    Box(modifier = Modifier
        .padding(start = 16.dp, top = 28.dp)
        .clickable {
            onDropDownStateChange(true)
        }) {
        Column {
            ImportanceDropDownLabel()
            ImportanceDropDownMenu(
                dropDownState, onDropDownStateChange, onDropDownSelected, selectedImportanceState
            )
        }
    }
}

@Composable
fun ImportanceDropDownLabel() {
    Text(
        text = stringResource(R.string.importance_drop_down),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = ToDoTaskTheme.colorScheme.labelPrimary
        )
    )
}

@Composable
fun ImportanceDropDownMenu(
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit,
    onDropDownSelected: (Importance) -> Unit,
    selectedImportanceState: Importance
) {
    Text(
        modifier = Modifier
            .alpha(0.7f)
            .padding(top = 4.dp),
        text = stringResource(selectedImportanceState.displayName),
        style = MaterialTheme.typography.headlineSmall.copy(
            color = ToDoTaskTheme.colorScheme.labelTertiary
        )
    )

    DropdownMenu(offset = DpOffset(0.dp, (-20).dp),
        modifier = Modifier.background(ToDoTaskTheme.colorScheme.backSecondary),
        expanded = dropDownState,
        onDismissRequest = { onDropDownStateChange(false) }) {
        Importance.entries.forEach { importance ->
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(importance.displayName),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ToDoTaskTheme.colorScheme.labelPrimary
                    )
                )
            }, onClick = {
                onDropDownSelected(importance)
                onDropDownStateChange(false)
            })
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
            modifier = Modifier
                .height(50.dp)
                .clickable(enabled = switchState) {
                        onDatePickerOnStateChange(true)
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            DeadlineLabel()
            DeadlineDatePicker(
                switchState,
                dateTextState,
                datePickerOnState,
                onDatePickerOnStateChange,
                onDateTextStateChange,
                dateState
            )
        }

        Switch(
            checked = switchState,
            onCheckedChange = onSwitchStateChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = ToDoTaskTheme.colorScheme.transparentBlue,
                checkedThumbColor = ToDoTaskTheme.colorScheme.colorBlue,
                uncheckedTrackColor = ToDoTaskTheme.colorScheme.supportOverlay,
                uncheckedThumbColor = ToDoTaskTheme.colorScheme.backElevated,
            )
        )
    }
}

@Composable
fun DeadlineLabel() {
    Text(
        text = stringResource(R.string.switch_deadline_descr),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = ToDoTaskTheme.colorScheme.labelPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineDatePicker(
    switchState: Boolean,
    dateTextState: String,
    datePickerOnState: Boolean,
    onDatePickerOnStateChange: (Boolean) -> Unit,
    onDateTextStateChange: (Long?) -> Unit,
    dateState: DatePickerState
) {
    if (switchState) {
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = dateTextState,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = ToDoTaskTheme.colorScheme.colorBlue
            )
        )
    }

    if (datePickerOnState) {
        DatePickerDialog(colors = DatePickerDefaults.colors(
            containerColor = ToDoTaskTheme.colorScheme.backSecondary
        ), onDismissRequest = { onDatePickerOnStateChange(false) }, confirmButton = {
            TextButton(onClick = {
                onDateTextStateChange(dateState.selectedDateMillis)
                onDatePickerOnStateChange(false)
            }) {
                Text(
                    stringResource(android.R.string.ok),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = ToDoTaskTheme.colorScheme.colorBlue
                    )
                )
            }
        }, dismissButton = {
            TextButton(onClick = { onDatePickerOnStateChange(false) }) {
                Text(
                    stringResource(android.R.string.cancel),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = ToDoTaskTheme.colorScheme.colorBlue
                    )
                )
            }
        }) {
            DatePicker(
                state = dateState, colors = DatePickerDefaults.colors(
                    titleContentColor = ToDoTaskTheme.colorScheme.colorBlue,
                    headlineContentColor = ToDoTaskTheme.colorScheme.colorBlue,
                    weekdayContentColor = ToDoTaskTheme.colorScheme.labelTertiary,
                    subheadContentColor = ToDoTaskTheme.colorScheme.colorBlue,
                    yearContentColor = ToDoTaskTheme.colorScheme.labelPrimary,
                    currentYearContentColor = ToDoTaskTheme.colorScheme.labelPrimary,
                    selectedYearContentColor = ToDoTaskTheme.colorScheme.labelPrimary,
                    dayContentColor = ToDoTaskTheme.colorScheme.labelPrimary,
                    selectedDayContentColor = ToDoTaskTheme.colorScheme.colorWhite,
                    todayContentColor = ToDoTaskTheme.colorScheme.labelPrimary,
                    selectedDayContainerColor = ToDoTaskTheme.colorScheme.colorBlue,
                    todayDateBorderColor = ToDoTaskTheme.colorScheme.colorBlue,
                )
            )
        }
    }
}

@Composable
fun DeleteTaskRow(itemId: String?, delete: (String) -> Unit, navigate: () -> Unit) {
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
                tint = ToDoTaskTheme.colorScheme.colorRed
            )
            Text(text = stringResource(R.string.delete_button),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ToDoTaskTheme.colorScheme.colorRed
                ),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        delete(itemId)
                        navigate()
                    }
            )
        }
    }
}
