package com.kionavani.todotask.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TodoItemsRepository {
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        val currentTime = System.currentTimeMillis()
        repositoryScope.launch {
            _todoItems.emit(
                listOf(
                    ToDoItem(
                        id = "1",
                        taskDescription = "Закончить проект",
                        isCompleted = false,
                        importance = Importance.HIGH,
                        creatingDate = currentTime,
                        deadlineDate = currentTime + 86400000 // плюс 1 день в миллисекундах
                    ),
                    ToDoItem(
                        id = "2",
                        taskDescription = "Сходить в магазин",
                        isCompleted = false,
                        importance = Importance.REGULAR,
                        creatingDate = currentTime,
                        deadlineDate = currentTime + 2 * 86400000 // плюс 2 дня в миллисекундах
                    ),
                    ToDoItem(
                        id = "3",
                        taskDescription = "Прочитать книгу",
                        isCompleted = true,
                        importance = Importance.LOW,
                        creatingDate = currentTime,
                        deadlineDate = currentTime + 3 * 86400000 // плюс 3 дня в миллисекундах
                    ),
                    ToDoItem(
                        id = "4",
                        taskDescription = "Посмотреть книгу, а потом написать " +
                                "для нее невероятную рецензию на мой любимый кинопоиск. " +
                                "Кстати, надеюсь это не будет багом из-за слишком длинной строки",
                        isCompleted = false,
                        importance = Importance.HIGH,
                        creatingDate = currentTime
                    ),
                    ToDoItem(
                        id = "5",
                        taskDescription = "Сгонять на пересдачу по матлогике",
                        isCompleted = false,
                        importance = Importance.LOW,
                        creatingDate = currentTime - 2 * 86400000, // минус 2 дня в миллисекундах
                        changingDate = currentTime - 86400000 // минус 1 день в миллисекундах
                    ),
                    ToDoItem(
                        id = "6",
                        taskDescription = "Сделать невероятно важное дело",
                        isCompleted = false,
                        importance = Importance.LOW,
                        creatingDate = currentTime - 2 * 86400000 // минус 2 дня в миллисекундах
                    ),
                    ToDoItem(
                        id = "7",
                        taskDescription = "Ничего не делать много времени",
                        isCompleted = true,
                        importance = Importance.HIGH,
                        creatingDate = currentTime
                    ),
                    ToDoItem(
                        id = "8",
                        taskDescription = "Спросить у ментора, что он ел на обед",
                        isCompleted = false,
                        importance = Importance.REGULAR,
                        creatingDate = currentTime - 86400000, // минус 1 день в миллисекундах
                        deadlineDate = currentTime - 7200000 // минус 2 часа в миллисекундах
                    ),
                    ToDoItem(
                        id = "9",
                        taskDescription = "Посмотреть лекцию по компоузу третий раз",
                        isCompleted = false,
                        importance = Importance.REGULAR,
                        creatingDate = currentTime - 4 * 86400000, // минус 4 дня в миллисекундах
                        deadlineDate = currentTime + 15 * 86400000 // плюс 15 дней в миллисекундах
                    ),
                    ToDoItem(
                        id = "10",
                        taskDescription = "Помыть воду из пятилитровки",
                        isCompleted = false,
                        importance = Importance.HIGH,
                        creatingDate = currentTime - 4 * 86400000, // минус 4 дня в миллисекундах
                        changingDate = currentTime - 3600000 // минус 1 час в миллисекундах
                    ),
                    ToDoItem(
                        id = "11",
                        taskDescription = "Создать 11 глупых тасков для теста приложения",
                        isCompleted = false,
                        importance = Importance.HIGH,
                        creatingDate = currentTime
                    ),
                )
            )
        }
    }

    fun addTodoItem(item: ToDoItem) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList + item)
        }
    }

    fun updateTodoItem(newItem: ToDoItem) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            val itemToCompare = currentList.find { it.id == newItem.id }
            if (itemToCompare != null) {
                val itemToAdd = itemToCompare.copy(
                    taskDescription = newItem.taskDescription,
                    importance = newItem.importance,
                    deadlineDate = newItem.deadlineDate,
                    changingDate = newItem.changingDate
                )
                _todoItems.emit(currentList.map {
                    if (it.id == itemToAdd.id) itemToAdd else it
                })
            }
        }
    }

    fun deleteTodoItem(itemId: String) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList.filter { it.id != itemId })
        }
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList.map { item ->
                if (item.id == itemId) item.copy(isCompleted = isCompleted) else item
            })
        }
    }

    fun getTaskById(itemId: String) = _todoItems.value.find { it.id == itemId }

    fun getNextId(): String = (_todoItems.value.last().id.toInt() + 1).toString()
}
