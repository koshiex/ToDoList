package com.kionavani.todotask.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.ui.ErrorState.FetchingError
import com.kionavani.todotask.ui.ErrorState.UpdatingError
import com.kionavani.todotask.ui.Util
import com.kionavani.todotask.ui.removeAllEmojis
import com.kionavani.todotask.ui.theme.ToDoTaskTheme
import com.kionavani.todotask.ui.viewmodels.MainScreenViewModel

/**
 * UI главного экрана для отображения списка тасок
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel, navigateToAdd: (String?) -> Unit, navigateToSettings: () -> Unit
) {
    val tasks by viewModel.todoItems.collectAsStateWithLifecycle()
    val completedCount by viewModel.completedTaskCounter.collectAsStateWithLifecycle()
    val isFiltering by viewModel.isFiltering.collectAsStateWithLifecycle()
    val errorState by viewModel.errorFlow.collectAsStateWithLifecycle()
    val loadingState by viewModel.isDataLoading.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val changeFiltering = { viewModel.changeFiltering() }
    val isCollapsing: Boolean by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction == 1f
        }
    }


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onFetchErrorClick = {
        viewModel.fetchData()
        viewModel.errorProcessed()
    }
    val onDismissClick = {
        viewModel.continueOffline()
    }
    val fetchingErrorMessage = stringResource(R.string.fetching_error)
    val updatingErrorMessage = stringResource(R.string.updating_error)
    val retryStr = stringResource(R.string.retry)

    LaunchedEffect(errorState) {
        when (errorState) {
            is FetchingError -> showSnackBar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                onActionClick = onFetchErrorClick,
                message = fetchingErrorMessage,
                duration = SnackbarDuration.Indefinite,
                onActionMessage = retryStr,
                onDismissClick = onDismissClick,
            )

            is UpdatingError -> showSnackBar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                message = updatingErrorMessage,
                duration = SnackbarDuration.Short
            )
        }
    }


    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .semantics { isTraversalGroup = true },
        topBar = {
            TopAppBar(
                scrollBehavior,
                isCollapsing,
                completedCount,
                isFiltering,
                changeFiltering,
                navigateToSettings
            )
        }, snackbarHost = {
            CustomSnackbarHost(
                snackbarHostState, stringResource(R.string.offline_mode)
            )
        }, floatingActionButton = { AddTaskFab(navigateToAdd) }) { paddingValues ->
        MainScreenContent(
            tasks = tasks,
            paddingValues = paddingValues,
            navigate = navigateToAdd,
            changeTaskState = { id, completed ->
                viewModel.toggleTaskCompletion(
                    id, completed
                )
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f), contentAlignment = Alignment.TopCenter
        ) {
            IndeterminateCircularIndicator(loadingState)
        }
    }
}


@Composable
fun IndeterminateCircularIndicator(isLoading: Boolean) {
    if (!isLoading) return
    val contentDescription = stringResource(R.string.indicator_descr)

    CircularProgressIndicator(
        modifier = Modifier
            .width(32.dp)
            .padding(top = 16.dp)
            .semantics { this.contentDescription = contentDescription },
        color = ToDoTaskTheme.colorScheme.colorBlue,
        trackColor = ToDoTaskTheme.colorScheme.supportSeparator,
    )
}

@Composable
fun CustomSnackbarHost(snackbarHostState: SnackbarHostState, dismissMassage: String = "") {
    SnackbarHost(hostState = snackbarHostState) {
        CustomSnackbar(
            snackbarData = it,
            containerColor = ToDoTaskTheme.colorScheme.colorRed,
            contentColor = ToDoTaskTheme.colorScheme.colorWhite,
            actionColor = ToDoTaskTheme.colorScheme.colorWhite,
            dismissMessage = dismissMassage
        )
    }
}

@Composable
fun AddTaskFab(navigate: (String?) -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .padding(end = 12.dp, bottom = 24.dp)
            .semantics { traversalIndex = -0.1f },
        shape = CircleShape,
        containerColor = ToDoTaskTheme.colorScheme.colorBlue,
        contentColor = ToDoTaskTheme.colorScheme.colorWhite,
        onClick = { navigate(null) }) {
        Icon(Icons.Filled.Add, stringResource(R.string.add_new_task_btn_descr))
    }
}

@Composable
fun MainScreenContent(
    tasks: List<ToDoItem>,
    paddingValues: PaddingValues,
    changeTaskState: (String, Boolean) -> Unit,
    navigate: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .background(ToDoTaskTheme.colorScheme.backPrimary)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        TaskList(tasks, changeTaskState, navigate)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isCollapsed: Boolean,
    completedCount: Int,
    isFiltering: Boolean,
    changeFiltering: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val scrollFraction by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction
        }
    }

    val animatedStartPadding by animateDpAsState(
        targetValue = lerp(88.dp, 16.dp, scrollFraction),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val animatedTopPadding by animateDpAsState(
        targetValue = lerp(50.dp, 0.dp, scrollFraction),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val animatedShadow by animateDpAsState(
        targetValue = lerp(0.dp, 4.dp, scrollFraction),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )


    LargeTopAppBar(title = {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    start = animatedStartPadding, top = animatedTopPadding
                )
                .animateContentSize()
                .semantics(mergeDescendants = true) {
                    heading()
                    isTraversalGroup = true
                    traversalIndex = -1f
                }

        ) {
            Text(
                text = stringResource(R.string.my_tasks_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = ToDoTaskTheme.colorScheme.labelPrimary
                )
            )
            AnimatedVisibility(
                visible = !isCollapsed,
                enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                exit = fadeOut(spring(stiffness = Spring.StiffnessLow))
            ) {
                Text(
                    modifier = Modifier
                        .alpha(0.8f)
                        .padding(top = 8.dp),
                    text = stringResource(R.string.completed_task_title) + " - $completedCount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ToDoTaskTheme.colorScheme.labelTertiary
                    ),
                )
            }
        }
    }, actions = {
        SettingsIcon(navigateToSettings)
        FilteringIcon(isFiltering, changeFiltering)
    }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = ToDoTaskTheme.colorScheme.backPrimary,
        scrolledContainerColor = ToDoTaskTheme.colorScheme.backSecondary,
    ), modifier = Modifier
        .shadow(animatedShadow)
        .semantics {
            isTraversalGroup = true
        }
    )
}

@Composable
fun SettingsIcon(
    navigateToSettings: () -> Unit
) {
    IconButton(onClick = navigateToSettings) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.settings_icon),
            contentDescription = stringResource(R.string.settings_btn_desc),
            tint = ToDoTaskTheme.colorScheme.colorBlue
        )
    }
}

@Composable
fun FilteringIcon(
    isFiltering: Boolean, changeFiltering: () -> Unit
) {
    IconButton(onClick = changeFiltering) {
        if (isFiltering) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.visibility_on_icon),
                contentDescription = stringResource(R.string.all_tasks_btn_desc),
                tint = ToDoTaskTheme.colorScheme.colorBlue
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.visibility_off_icon),
                contentDescription = stringResource(R.string.hide_completed_btn_desc),
                tint = ToDoTaskTheme.colorScheme.colorBlue
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
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = ToDoTaskTheme.colorScheme.backSecondary, shape = RoundedCornerShape(12.dp)
            ), state = listState
    ) {

        items(tasks) { task ->
            AnimatedContent(
                targetState = task,
            ) { item ->
                TaskItem(item, changeTaskState, getDeadlineDate, navigate)
            }

        }
        item {
            Text(
                text = stringResource(R.string.new_task_button),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 22.dp, start = 76.dp)
                    .alpha(0.3f)
                    .clickable(onClickLabel = stringResource(R.string.add_new_task_btn_descr)) {
                        navigate(null)
                    }
                    .semantics {
                        heading()
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
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        ItemCheckbox(item, deadlineDate, changeTaskState)
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
                .weight(1f)
                .clearAndSetSemantics {}
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
                contentDescription = stringResource(R.string.change_task_btn_descr),
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
fun ItemCheckbox(
    item: ToDoItem,
    deadlineDate: String?,
    changeTaskState: (String, Boolean) -> Unit
) {
    val completedDescription = stringResource(R.string.completed_descr)
    val uncompletedDescription = stringResource(R.string.uncompleted_descr)
    val deadlineDescription =
        if (deadlineDate != null) stringResource(R.string.switch_deadline_descr) + deadlineDate
        else ""
    val importanceDescription = stringResource(item.importance.description)

    Checkbox(modifier = Modifier
        .padding(start = 16.dp, top = 12.dp)
        .semantics {
            contentDescription =
                (if (item.isCompleted) completedDescription else uncompletedDescription) +
                        "\n$importanceDescription" +
                        "\n${item.taskDescription.removeAllEmojis()}" +
                        "\n $deadlineDescription"
        },
        checked = item.isCompleted,
        colors = CheckboxDefaults.colors(
            uncheckedColor = if (item.importance == Importance.HIGH) ToDoTaskTheme.colorScheme.colorRed
            else ToDoTaskTheme.colorScheme.supportSeparator,
            checkedColor = ToDoTaskTheme.colorScheme.colorGreen,
            checkmarkColor = ToDoTaskTheme.colorScheme.backSecondary
        ),
        onCheckedChange = { changeTaskState(item.id, it) })
}
