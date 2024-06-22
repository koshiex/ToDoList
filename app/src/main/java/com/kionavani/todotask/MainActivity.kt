package com.kionavani.todotask

import MainScreen
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kionavani.todotask.ui.AddTaskScreen
import com.kionavani.todotask.ui.theme.ToDoTaskTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        val tasksRepository = TodoItemsRepository()
        val items = tasksRepository.getTodoItems()
        setContent {
            ToDoTaskTheme {
//                MainScreen(items = items)
                AddTaskScreen()
            }
        }
    }
}



