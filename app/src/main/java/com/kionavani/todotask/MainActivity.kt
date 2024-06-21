package com.kionavani.todotask

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.core.view.WindowCompat
import com.kionavani.todotask.ui.theme.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        val tasksRepository = TodoItemsRepository()
        setContent {
            ToDoTaskTheme {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    TaskList(tasks = tasksRepository.getTodoItems())
                }
            }
        }
    }
}

@Composable
fun TaskList(tasks: List<ToDoItem>) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
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

                }
            )
        }
    }
}

@Composable
fun Task(item: ToDoItem) {
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
                .padding(start = 12.dp, top = 24.dp)
                .weight(1f)
        )


        IconButton(
            modifier = Modifier
                .padding(end = 16.dp, top = 12.dp)
                .alpha(0.6f),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

