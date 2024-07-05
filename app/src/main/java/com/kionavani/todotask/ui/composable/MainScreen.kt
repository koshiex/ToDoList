package com.kionavani.todotask.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.ui.LocalNavController
import com.kionavani.todotask.ui.Util
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val navController = LocalNavController.current
    val tasks by viewModel.todoItems.collectAsState()
    val completedCount by viewModel.completedTaskCounter.collectAsState()
    val isFiltering by viewModel.isFiltering.collectAsState()
    val isErrorHappened by viewModel.isErrorHappened.collectAsState()
    Log.e("Compose", isErrorHappened.toString())


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onFetchErrorClick = {
        viewModel.fetchData()
        viewModel.errorProcessed()
    }
    val fetchingErrorMessage = stringResource(R.string.fetching_error)
    val retryStr = stringResource(R.string.retry)

    val changeTaskState = { itemId: String, isCompleted: Boolean ->
        viewModel.toggleTaskCompletion(itemId, isCompleted)
    }
    val getDeadlineDate = { item: ToDoItem ->
        item.deadlineDate?.let { Util.dateToString(it) }
    }

    LaunchedEffect(isErrorHappened) {
        if (isErrorHappened) {
            showSnackBar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                onClick = onFetchErrorClick,
                message = fetchingErrorMessage,
                onActionMessage = retryStr
            )
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    actionColor = MaterialTheme.colorScheme.onError,
                )

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 24.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                contentColor = Color.White,
                onClick = {
                    navController.navigate(AddTaskScreenNav(null))
                }
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.my_tasks_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .padding(top = 50.dp, start = 88.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 24.dp, start = 88.dp, bottom = 18.dp)
            ) {
                Text(
                    text =
                    stringResource(R.string.completed_task_title) +
                            " - $completedCount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .alpha(0.8f)
                )

                IconButton(
                    onClick = {
                        viewModel.changeFiltering()
                    }
                ) {
                    Icon(
                        imageVector =
                        if (isFiltering) {
                            ImageVector.vectorResource(R.drawable.visibility_on_icon)
                        } else {
                            ImageVector.vectorResource(R.drawable.visibility_off_icon)
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }

            TaskList(
                tasks = tasks,
                changeTaskState,
                getDeadlineDate
            )
        }
    }
}


@Composable
fun TaskList(
    tasks: List<ToDoItem>,
    changeTaskState: (String, Boolean) -> Unit,
    getDeadlineDate: (ToDoItem) -> String?,
) {
    val navController = LocalNavController.current

    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
    ) {
        items(tasks) { task ->
            Task(item = task, changeTaskState, getDeadlineDate)

        }
        item {
            Text(
                text = stringResource(R.string.new_task_button),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 22.dp, start = 76.dp)
                    .alpha(0.3f)
                    .clickable {
                        navController.navigate(AddTaskScreenNav(null))
                    },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                )
            )
        }
    }
}

@Composable
fun Task(
    item: ToDoItem,
    changeTaskState: (String, Boolean) -> Unit,
    getDeadlineDate: (ToDoItem) -> String?,
) {
    val navController = LocalNavController.current
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

            if (deadlineDate != null) {
                Text(
                    text = deadlineDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }


        IconButton(
            modifier = Modifier
                .padding(end = 16.dp, top = 12.dp)
                .alpha(0.6f),
            onClick = { navController.navigate(AddTaskScreenNav(item.id)) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
fun ItemDescription(description: String, isCompleted: Boolean) {
    if (isCompleted) {
        Text(
            text = description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiary,
            textDecoration = TextDecoration.LineThrough
        )
    } else {
        Text(
            text = description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

// TODO: Заменить на кастомный чекбокс
@Composable
fun ItemCheckbox(item: ToDoItem, changeTaskState: (String, Boolean) -> Unit) {
    if (item.importance == Importance.HIGH) {
        Checkbox(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            checked = item.isCompleted,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.error,
                checkedColor = MaterialTheme.colorScheme.inversePrimary,
                checkmarkColor = MaterialTheme.colorScheme.secondary
            ),
            onCheckedChange = {
                changeTaskState(item.id, it)
            }
        )
    } else {
        Checkbox(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            checked = item.isCompleted,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkedColor = MaterialTheme.colorScheme.inversePrimary,
                checkmarkColor = MaterialTheme.colorScheme.secondary
            ),
            onCheckedChange = {
                changeTaskState(item.id, it)
            }
        )
    }
}

fun showSnackBar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onClick: () -> Unit,
    message: String,
    onActionMessage: String
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = onActionMessage,
                duration = SnackbarDuration.Indefinite
            )
        when (result) {
            SnackbarResult.ActionPerformed -> onClick()
            SnackbarResult.Dismissed -> Unit
        }
    }
}