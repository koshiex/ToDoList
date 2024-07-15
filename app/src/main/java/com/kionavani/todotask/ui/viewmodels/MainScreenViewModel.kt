package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.ui.NetworkMonitor
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ErrorState
import com.kionavani.todotask.ui.ErrorState.*
import com.kionavani.todotask.ui.ResourcesProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel для управления состоянием главного экрана и операциями, связанными с отображением тасок
 */
class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val provider: ResourcesProvider,
    private val networkMonitor: NetworkMonitor
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

    private val _isDataLoading = MutableStateFlow(false)
    val isDataLoading = _isDataLoading.asStateFlow()

    private val _errorFlow = MutableStateFlow<ErrorState>(ErrorProcessed())
    val errorFlow = _errorFlow.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _errorFlow.value = FetchingError()
            needToFetch = true
            changeLoadingState()
        }
    }

    private var needToFetch = true

    init {
        viewModelScope.launch(exceptionHandler) {
            launch {
                _todoItems.collectLatest {
                    updateCompletedCount(it)
                }
            }
            launch {
                networkMonitor.isConnected.collectLatest {
                    repository.changeNetworkStatus(it)
                }
            }
        }
    }

    fun changeLoadingState() {
        _isDataLoading.value = !_isDataLoading.value
    }

    fun continueOffline() {
        viewModelScope.launch(exceptionHandler) {
            repository.changeNetworkStatus(false)
            fetchData()
        }
    }

    fun errorProcessed() {
        _errorFlow.value = ErrorProcessed()
    }

    fun setUpdatingError() {
        _errorFlow.value = UpdatingError()
    }

    fun fetchData() {
        if (needToFetch) {
            needToFetch = false
            changeLoadingState()
            viewModelScope.launch(exceptionHandler) {
                repository.fetchData()
                changeLoadingState()
            }
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
