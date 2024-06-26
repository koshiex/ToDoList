package com.kionavani.todotask

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class TodoItemsRepository {
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    init {
        _todoItems.value = mutableListOf(
            ToDoItem(
                id = "1",
                taskDescription = "Закончить проект",
                isCompleted = false,
                importanceTask = Importance.HIGH,
                creatingDate = LocalDateTime.now(),
                deadlineDate = LocalDateTime.now().plusDays(1)
            ),
            ToDoItem(
                id = "2",
                taskDescription = "Сходить в магазин",
                isCompleted = false,
                importanceTask = Importance.REGULAR,
                creatingDate = LocalDateTime.now(),
                deadlineDate = LocalDateTime.now().plusDays(2)
            ),
            ToDoItem(
                id = "3",
                taskDescription = "Прочитать книгу",
                isCompleted = true,
                importanceTask = Importance.LOW,
                creatingDate = LocalDateTime.now(),
                deadlineDate = LocalDateTime.now().plusDays(3)
            ),
            ToDoItem(
                id = "4",
                taskDescription = "Посмотреть книгу, а потом написать " +
                        "для нее невероятную рецензию на мой любимый кинопоиск. " +
                        "Кстати, надеюсь это не будет багом из за слишком длинной строки",
                isCompleted = false,
                importanceTask = Importance.HIGH,
                creatingDate = LocalDateTime.now(),
            ),
            ToDoItem(
                id = "5",
                taskDescription = "Сгонять на пересдачу по матлогике",
                isCompleted = false,
                importanceTask = Importance.LOW,
                creatingDate = LocalDateTime.now().minusDays(2),
                changingDate = LocalDateTime.now().minusDays(1)
            ),
            ToDoItem(
                id = "6",
                taskDescription = "Сделать невероятно важное дело",
                isCompleted = false,
                importanceTask = Importance.LOW,
                creatingDate = LocalDateTime.now().minusDays(2),
            ),
            ToDoItem(
                id = "7",
                taskDescription = "Ничего не делать много времени",
                isCompleted = true,
                importanceTask = Importance.HIGH,
                creatingDate = LocalDateTime.now(),
            ),
            ToDoItem(
                id = "8",
                taskDescription = "Спросить у ментора, что он ел на обед",
                isCompleted = false,
                importanceTask = Importance.REGULAR,
                creatingDate = LocalDateTime.now().minusDays(1),
                deadlineDate = LocalDateTime.now().minusHours(2)
            ),
            ToDoItem(
                id = "9",
                taskDescription = "Посмотреть лекцию по компоузу третий раз",
                isCompleted = false,
                importanceTask = Importance.REGULAR,
                creatingDate = LocalDateTime.now().minusDays(4),
                deadlineDate = LocalDateTime.now().plusDays(15)
            ),
            ToDoItem(
                id = "10",
                taskDescription = "Помыть воду из пятилитровки",
                isCompleted = false,
                importanceTask = Importance.HIGH,
                creatingDate = LocalDateTime.now().minusDays(4),
                changingDate = LocalDateTime.now().minusHours(1)
            ),
            ToDoItem(
                id = "11",
                taskDescription = "Создать 11 глупых тасков для теста приложения",
                isCompleted = false,
                importanceTask = Importance.HIGH,
                creatingDate = LocalDateTime.now(),
            ),
        )
    }

    fun addTodoItem(item: ToDoItem) {
        _todoItems.value += item
    }

    fun updateTodoItem(newItem: ToDoItem) {
        val itemToCompare = _todoItems.value.find { it.id == newItem.id }

        if (itemToCompare != null) {
            newItem.isCompleted = itemToCompare.isCompleted
            newItem.creatingDate = itemToCompare.creatingDate

            _todoItems.value = _todoItems.value.map {
                if (newItem.id == it.id) {
                    newItem
                } else {
                    it
                }
            }
        }
    }

    fun deleteTodoItem(itemId: String) {
        _todoItems.value = _todoItems.value.filter { it.id != itemId }
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        _todoItems.value = _todoItems.value.map { item ->
            if (item.id == itemId) item.copy(isCompleted = isCompleted) else item
        }
    }

    // Почему в тз айди - строка???
    fun getNextId(): String = (_todoItems.value.last().id.toInt() + 1).toString()


}