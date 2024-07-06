package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ErrorState
import com.kionavani.todotask.ui.ErrorState.*
import com.kionavani.todotask.ui.ResourcesProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val provider: ResourcesProvider,
) : ViewModel() {
    private val _todoItems = repository.todoItems
        .map { list -> list.map { it.copy(taskDescription = getDescWithEmoji(it)) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isFiltering = MutableStateFlow(false)
    val isFiltering: StateFlow<Boolean> = _isFiltering.asStateFlow()

    val todoItems: StateFlow<List<ToDoItem>> = combine(_todoItems, _isFiltering) { items, isFiltering ->
        if (isFiltering) {
            items.filterNot { it.isCompleted }
        } else {
            items
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _completedTaskCounter = MutableStateFlow(0)
    val completedTaskCounter: StateFlow<Int> = _completedTaskCounter.asStateFlow()

    private val _errorFlow = MutableStateFlow<ErrorState>(ErrorProcessed())
    val errorFlow = _errorFlow.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _errorFlow.value = FetchingError()
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            _todoItems.collectLatest {
                updateCompletedCount(it)
            }
        }
    }

    fun errorProcessed() {
        _errorFlow.value = ErrorProcessed()
    }

    fun setUpdatingError() {
        _errorFlow.value = UpdatingError()
    }

    fun fetchData() {
        viewModelScope.launch(exceptionHandler) {
            repository.fetchData()
        }
    }

    fun changeFiltering() {
        _isFiltering.value = !_isFiltering.value
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            repository.toggleTaskCompletion(itemId, isCompleted)
        }
    }

    private fun updateCompletedCount(items : List<ToDoItem>) {
        _completedTaskCounter.value = items.count { it.isCompleted }
    }

    private fun getDescWithEmoji(item: ToDoItem): String {
        return when (item.importance) {
            Importance.LOW ->
                provider.getString(R.string.low_importance_emoji) + " ${item.taskDescription}"

            Importance.REGULAR ->
                item.taskDescription

            Importance.HIGH ->
                provider.getString(R.string.high_importance_emoji) + " ${item.taskDescription}"
        }
    }


}
