package com.kionavani.todotask.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.ui.ErrorState.FetchingError
import com.kionavani.todotask.ui.ErrorState.UpdatingError
import com.kionavani.todotask.ui.Util
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel

/**
 * UI главного экрана для отображения списка тасок
 */
@Composable
fun MainScreen(viewModel: MainScreenViewModel, navigate: (String?) -> Unit) {
    val tasks by viewModel.todoItems.collectAsState()
    val completedCount by viewModel.completedTaskCounter.collectAsState()
    val isFiltering by viewModel.isFiltering.collectAsState()
    val errorState by viewModel.errorFlow.collectAsState()
    val loadingState by viewModel.isDataLoading.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onFetchErrorClick = {
        viewModel.fetchData()
        viewModel.errorProcessed()
    }
    val fetchingErrorMessage = stringResource(R.string.fetching_error)
    val updatingErrorMessage = stringResource(R.string.updating_error)
    val retryStr = stringResource(R.string.retry)

    LaunchedEffect(errorState) {
        when (errorState) {
            is FetchingError -> showSnackBar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                onClick = onFetchErrorClick,
                message = fetchingErrorMessage,
                duration = SnackbarDuration.Indefinite,
                onActionMessage = retryStr
            )

            is UpdatingError -> showSnackBar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                message = updatingErrorMessage,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        floatingActionButton = { AddTaskFab(navigate) }) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            MainScreenContent(
                tasks = tasks,
                completedCount = completedCount,
                isFiltering = isFiltering,
                paddingValues = paddingValues,
                changeFiltering = { viewModel.changeFiltering() },
                changeTaskState = { id, completed ->
                    viewModel.toggleTaskCompletion(
                        id, completed
                    )
                },
                navigate
            )
            IndeterminateCircularIndicator(isLoading = loadingState)
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(isLoading: Boolean) {
    if (!isLoading) return

    CircularProgressIndicator(
        modifier = Modifier
            .width(32.dp)
            .padding(top = 60.dp),
        color = ToDoTaskTheme.colorScheme.colorBlue,
        trackColor = ToDoTaskTheme.colorScheme.supportSeparator,
    )
}

@Composable
fun CustomSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) {
        CustomSnackbar(
            snackbarData = it,
            containerColor = ToDoTaskTheme.colorScheme.colorRed,
            contentColor = ToDoTaskTheme.colorScheme.colorWhite,
            actionColor = ToDoTaskTheme.colorScheme.colorWhite
        )
    }
}

@Composable
fun AddTaskFab(navigate: (String?) -> Unit) {
    FloatingActionButton(modifier = Modifier.padding(end = 12.dp, bottom = 24.dp),
        shape = CircleShape,
        containerColor = ToDoTaskTheme.colorScheme.colorBlue,
        contentColor = ToDoTaskTheme.colorScheme.colorWhite,
        onClick = { navigate(null) }) {
        Icon(Icons.Filled.Add, null)
    }
}

@Composable
fun MainScreenContent(
    tasks: List<ToDoItem>,
    completedCount: Int,
    isFiltering: Boolean,
    paddingValues: PaddingValues,
    changeFiltering: () -> Unit,
    changeTaskState: (String, Boolean) -> Unit,
    navigate: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .background(ToDoTaskTheme.colorScheme.backPrimary)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Header(completedCount, isFiltering, changeFiltering)
        TaskList(tasks, changeTaskState, navigate)
    }
}

@Composable
fun Header(
    completedCount: Int, isFiltering: Boolean, changeFiltering: () -> Unit
) {
    Text(
        text = stringResource(R.string.my_tasks_title),
        style = MaterialTheme.typography.titleLarge.copy(
            color = ToDoTaskTheme.colorScheme.labelPrimary
        ),
        modifier = Modifier.padding(top = 50.dp, start = 88.dp)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(end = 24.dp, start = 88.dp, bottom = 18.dp)
    ) {
        Text(
            text = stringResource(R.string.completed_task_title) + " - $completedCount",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = ToDoTaskTheme.colorScheme.labelTertiary
            ),
            modifier = Modifier.alpha(0.8f)
        )

        IconButton(onClick = changeFiltering) {
            Icon(
                imageVector = if (isFiltering) {
                    ImageVector.vectorResource(R.drawable.visibility_on_icon)
                } else {
                    ImageVector.vectorResource(R.drawable.visibility_off_icon)
                }, contentDescription = null, tint = ToDoTaskTheme.colorScheme.colorBlue
            )
        }
    }
}

@Composable
fun TaskList(
    tasks: List<ToDoItem>, changeTaskState: (String, Boolean) -> Unit, navigate: (String?) -> Unit
) {
    val getDeadlineDate = { item: ToDoItem ->
        item.deadlineDate?.let { Util.dateToString(it) }
    }

    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = ToDoTaskTheme.colorScheme.backSecondary, shape = RoundedCornerShape(12.dp)
            ),
    ) {
        items(tasks) { task ->
            TaskItem(task, changeTaskState, getDeadlineDate, navigate)
        }
        item {
            Text(
                text = stringResource(R.string.new_task_button),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 22.dp, start = 76.dp)
                    .alpha(0.3f)
                    .clickable {
                        navigate(null)
                    },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ToDoTaskTheme.colorScheme.labelTertiary
                )
            )
        }
    }
}

@Composable
fun TaskItem(
    item: ToDoItem,
    changeTaskState: (String, Boolean) -> Unit,
    getDeadlineDate: (ToDoItem) -> String?,
    navigate: (String?) -> Unit
) {
    val deadlineDate = getDeadlineDate(item)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        ItemCheckbox(item, changeTaskState)
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
                .weight(1f)
        ) {
            ItemDescription(item.taskDescription, item.isCompleted)
            deadlineDate?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = ToDoTaskTheme.colorScheme.labelTertiary
                )
            }
        }
        IconButton(modifier = Modifier
            .padding(end = 16.dp, top = 12.dp)
            .alpha(0.6f),
            onClick = { navigate(item.id) }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                contentDescription = null,
                tint = ToDoTaskTheme.colorScheme.labelTertiary
            )
        }
    }
}

@Composable
fun ItemDescription(description: String, isCompleted: Boolean) {
    Text(
        text = description,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyMedium,
        color = if (isCompleted) ToDoTaskTheme.colorScheme.labelTertiary
            else ToDoTaskTheme.colorScheme.labelPrimary,
        textDecoration = if (isCompleted) TextDecoration.LineThrough else null
    )
}

@Composable
fun ItemCheckbox(item: ToDoItem, changeTaskState: (String, Boolean) -> Unit) {
    Checkbox(modifier = Modifier.padding(start = 16.dp, top = 12.dp),
        checked = item.isCompleted,
        colors = CheckboxDefaults.colors(
            uncheckedColor = if (item.importance == Importance.HIGH) ToDoTaskTheme.colorScheme.colorRed
                else ToDoTaskTheme.colorScheme.supportSeparator,
            checkedColor = ToDoTaskTheme.colorScheme.colorGreen,
            checkmarkColor = ToDoTaskTheme.colorScheme.backSecondary
        ),
        onCheckedChange = { changeTaskState(item.id, it) })
}
