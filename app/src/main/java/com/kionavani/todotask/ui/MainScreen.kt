import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kionavani.todotask.ui.LocalNavController
import com.kionavani.todotask.R
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.ui.viewmodels.ToDoViewModel
import com.kionavani.todotask.ui.AddTaskScreenNav
import com.kionavani.todotask.ui.theme.LightBlue

@Composable
fun MainScreen(viewModel: ToDoViewModel) {
    val navController = LocalNavController.current
    val tasks by viewModel.todoItems.collectAsState()

    val changeTaskState = { itemId: String, isCompleted: Boolean ->
        viewModel.toggleTaskCompletion(itemId, isCompleted)
    }
    val getDeadlineDate = { item: ToDoItem ->
        item.deadlineDate?.let { viewModel.dateToString(it) }
    }

    var visibilityButtonState by remember {
        mutableStateOf(true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 24.dp),
                shape = CircleShape,
                containerColor = LightBlue,
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
                            " - ${viewModel.getCompletedCount()}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .alpha(0.8f)
                )

                IconButton(
                    onClick = {
                        visibilityButtonState = !visibilityButtonState
                    }
                ) {
                    Icon(
                        imageVector =
                        if (visibilityButtonState) {
                            ImageVector.vectorResource(R.drawable.visibility_on_icon)
                        } else {
                            ImageVector.vectorResource(R.drawable.visibility_off_icon)
                        },
                        contentDescription = null,
                        tint = LightBlue
                    )
                }
            }

            TaskList(
                tasks = if (!visibilityButtonState) {
                    viewModel.filterTasksByCompleted(tasks)
                } else {
                    tasks
                },
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
    getDeadlineDate: (ToDoItem) -> String?
) {
    val navController = LocalNavController.current

    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .wrapContentSize()
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
            ClickableText(
                text = AnnotatedString(stringResource(R.string.new_task_button)),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 22.dp, start = 76.dp)
                    .alpha(0.3f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    navController.navigate(AddTaskScreenNav(null))
                }
            )
        }
    }
}

@Composable
fun Task(
    item: ToDoItem,
    changeTaskState: (String, Boolean) -> Unit,
    getDeadlineDate: (ToDoItem) -> String?
) {
    val navController = LocalNavController.current
    val deadlineDate = getDeadlineDate(item)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        Checkbox(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),    // TODO : Checkbox colors
            checked = item.isCompleted,
            onCheckedChange = {
                changeTaskState(item.id, it)
            }
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = item.taskDescription,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )

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