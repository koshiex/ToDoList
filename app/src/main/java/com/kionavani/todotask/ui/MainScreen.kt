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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
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
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kionavani.todotask.LocalNavController
import com.kionavani.todotask.R
import com.kionavani.todotask.ToDoItem
import com.kionavani.todotask.ui.AddTaskScreenNav

@Composable
fun MainScreen(items: List<ToDoItem>) {
    val navController = LocalNavController.current

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 24.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                onClick = {
                    navController.navigate(AddTaskScreenNav)
                }
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.my_tasks_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
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
                    text = stringResource(R.string.completed_task_title),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .alpha(0.8f)
                )

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.visibility_on_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            TaskList(tasks = items)
        }
    }
}


@Composable
fun TaskList(tasks: List<ToDoItem>) {
    val navController = LocalNavController.current

    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .wrapContentSize()
            .shadow(1.dp, shape = RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ),
    ) {
        items(tasks) { task ->
            Task(item = task)
        }
        item {
            ClickableText(
                text = AnnotatedString(stringResource(R.string.new_task_button)),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 22.dp, start = 76.dp)
                    .alpha(0.3f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    navController.navigate(AddTaskScreenNav)
                }
            )
        }
    }
}

@Composable
fun Task(item: ToDoItem) {
    val navController = LocalNavController.current

    var checkedState by remember { mutableStateOf(item.isCompleted) }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Checkbox(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            checked = checkedState,
            onCheckedChange = {
                item.isCompleted = it // TODO: перенести в другое место
                checkedState = it
            }
        )

        Text(
            text = item.taskDescription,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .alignBy(FirstBaseline)
                .padding(start = 12.dp, top = 30.dp)
                .weight(1f)
        )


        IconButton(
            modifier = Modifier
                .padding(end = 16.dp, top = 12.dp)
                .alpha(0.6f),
            onClick = { navController.navigate(AddTaskScreenNav) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}