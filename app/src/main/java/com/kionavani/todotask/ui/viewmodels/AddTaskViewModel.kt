package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import com.kionavani.todotask.ui.Util
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel для управления состоянием и операциями, связанными с добавлением или обновлением таски
 */

class AddTaskViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _isErrorHappened = MutableStateFlow(false)
    val isErrorHappened = _isErrorHappened.asStateFlow()

    private val _dataChanged = MutableStateFlow(false)
    var dataChanged = _dataChanged.asStateFlow()

    private val _textFieldState = MutableStateFlow("")
    val textFieldState = _textFieldState.asStateFlow()

    private val _switchState = MutableStateFlow(false)
    val switchState = _switchState.asStateFlow()

    private val _dateTextState = MutableStateFlow("")
    val dateTextState = _dateTextState.asStateFlow()

    private val _datePickerOnState = MutableStateFlow(false)
    val datePickerOnState = _datePickerOnState.asStateFlow()

    private val _deadlineDate = MutableStateFlow<Long?>(null)
    val deadlineDate = _deadlineDate.asStateFlow()

    private val _dropDownState = MutableStateFlow(false)
    val dropDownState = _dropDownState.asStateFlow()

    private val _selectedImportanceState = MutableStateFlow(Importance.REGULAR)
    val selectedImportanceState = _selectedImportanceState.asStateFlow()


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _isErrorHappened.value = true
        }
    }

    fun deleteTodoItem(itemId: String) {
        viewModelScope.launch(exceptionHandler) {
            repository.deleteTodoItem(itemId)
        }
    }

    private suspend fun getNextId(): String =
        repository.getNextId()

    fun loadTask(itemId: String) {
        viewModelScope.launch(exceptionHandler) {
            val task = repository.getTaskById(itemId)
            initializeState(task)
        }
    }

    fun errorProcessed() {
        _isErrorHappened.value = false
    }

    fun initializeState(task: ToDoItem?) {
        _textFieldState.value = task?.taskDescription ?: ""
        _switchState.value = task?.deadlineDate != null
        _dateTextState.value = task?.deadlineDate?.let { Util.dateToString(it) }
            ?: resourcesProvider.getString(R.string.deadline_selector)
        _deadlineDate.value = task?.deadlineDate
        _selectedImportanceState.value = task?.importance ?: Importance.REGULAR
    }

    fun onDropDownSelected(newImportance: Importance) {
        _selectedImportanceState.value = newImportance
    }

    fun addOrUpdateTask(itemId: String?) {
        viewModelScope.launch(exceptionHandler) {
            val item = ToDoItem(
                id = itemId ?: getNextId(),
                taskDescription = textFieldState.value,
                isCompleted = false,
                importance = selectedImportanceState.value,
                creatingDate = System.currentTimeMillis(),
                deadlineDate = deadlineDate.value
            )
            if (itemId != null) {
                repository.updateTodoItem(item)
            } else {
                repository.addTodoItem(item)
            }
        }
    }

    fun onTextChange(newText: String) {
        _textFieldState.value = newText
    }

    fun onSwitchStateChange(newState: Boolean) {
        _switchState.value = newState
    }

    fun onDateTextStateChange(date: Long?) {
        _dateTextState.value = date?.let { Util.dateToString(it) }
            ?: resourcesProvider.getString(R.string.deadline_selector)
        _deadlineDate.value = date
    }

    fun onDatePickerOnStateChange(newState: Boolean) {
        _datePickerOnState.value = newState
    }

    fun onDropDownStateChange(newState: Boolean) {
        _dropDownState.value = newState
    }
}