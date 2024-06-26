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
import androidx.navigation.toRoute
import com.kionavani.todotask.Importance
import com.kionavani.todotask.LocalNavController
import com.kionavani.todotask.R
import com.kionavani.todotask.ToDoItem
import com.kionavani.todotask.ToDoViewModel
import com.kionavani.todotask.ui.theme.LightBlue
import com.kionavani.todotask.ui.theme.LightRed
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: ToDoViewModel, itemID: String? = null) {
    val navController = LocalNavController.current

    val deadlineSelectorText = stringResource(R.string.deadline_selector)

    var textFiledState by remember { mutableStateOf("") }
    var dropDownState by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }

    var dateTextState by remember { mutableStateOf("") }
    var datePickerOnState by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    val parsedDate : LocalDateTime? = null
    if (dateTextState != "") {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDay = LocalDateTime.parse(dateTextState, dateFormatter)
    }


    var selectedImportanceState by remember {
        mutableStateOf(Importance.LOW)
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
            parsedDate
        )
        TaskTextField(textFiledState, onTextChange = { textFiledState = it })
        ImportanceDropDown(dropDownState,
            onDropDownStateChange = { dropDownState = it },
            onDropDownSelected = { selectedImportanceState = it })
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
        DeleteTaskRow(itemID) { itemID ->
            viewModel.deleteTodoItem(itemID)
        }
    }
}

@Composable
fun Header(
    viewModel: ToDoViewModel,
    itemId: String?,
    descr: String,
    importance: Importance,
    deadlineDate: LocalDateTime?
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
                color = LightBlue
            ),
            modifier = Modifier.padding(top = 16.dp, end = 16.dp)
        ) {
            if (itemId != null) {
                val item = ToDoItem(
                    itemId,
                    descr,
                    false,
                    importance,
                    LocalDateTime.now(),
                    deadlineDate,
                    LocalDateTime.now()
                )
                viewModel.updateTodoItem(item)
            } else {
                val item = ToDoItem(
                    viewModel.getNextId(),
                    descr,
                    false,
                    importance,
                    LocalDateTime.now(),
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
        value = textFiledState,
        onValueChange = onTextChange,
    )
}

@Composable
fun ImportanceDropDown(
    dropDownState: Boolean,
    onDropDownStateChange: (Boolean) -> Unit,
    onDropDownSelected: (Importance) -> Unit
) {
    Box(modifier = Modifier.padding(start = 16.dp, top = 28.dp)) {
        Column {
            Text(
                text = stringResource(R.string.importance_drop_down),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            ClickableText(
                modifier = Modifier.alpha(0.7f),
                text = AnnotatedString(stringResource(Importance.LOW.displayName)),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                onDropDownStateChange(true)
            }
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
                    onClick = { onDropDownSelected(importance) }
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
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            if (switchState) {
                ClickableText(
                    text = AnnotatedString(dateTextState),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = LightBlue
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
                            } ?: ""
                            onDateTextStateChange(formattedDate)
                            onDatePickerOnStateChange(false)
                        }) {
                            Text(
                                stringResource(android.R.string.ok),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onDatePickerOnStateChange(false) }) {
                            Text(
                                stringResource(android.R.string.cancel),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                ) {
                    DatePicker(state = dateState) // TODO : DatePickerColors
                }
            }
        }

        Switch(
            checked = switchState,
            onCheckedChange = onSwitchStateChange   // TODO: switch colors
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
                tint = LightRed
            )
            ClickableText(
                modifier = Modifier.padding(start = 12.dp),
                text = AnnotatedString(stringResource(R.string.delete_button)),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LightRed
                )
            ) {
                delete(itemId)
                navController.navigate(MainScreenNav)
            }
        }
    }
}
