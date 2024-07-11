package com.kionavani.todotask.data

import com.kionavani.todotask.data.database.TasksDb
import com.kionavani.todotask.data.remote.dto.ElementDto
import com.kionavani.todotask.data.remote.dto.ResponseDto.*
import com.kionavani.todotask.data.remote.dto.RequestDto.*

/**
 * Мапит данные из DTO сетевых ответов/запросов в модель UI-ая
 */
class TasksMapper {
    fun fromNetworkList(dto: ListElementResponseDto): List<ToDoItem> {
        val mapResult: MutableList<ToDoItem> = mutableListOf()

        for (element in dto.list) {
            mapResult += fromNetworkElement(element)
        }

        return mapResult
    }

    fun toRequestList(itemsList: List<ToDoItem>): ListElementRequestDto {
        val newList: MutableList<ElementDto> = mutableListOf()

        for (item in itemsList) {
            newList += toElement(item)
        }

        return ListElementRequestDto(newList)
    }

    fun toRequestElement(item: ToDoItem) = SingleElementRequestDto(toElement(item))

    fun fromNetworkElement(element: ElementDto): ToDoItem = ToDoItem(
        element.id,
        element.text,
        element.done,
        element.importance.toImportance(),
        element.created_at,
        element.deadline,
        element.changed_at
    )

    fun toDatabaseElement(item: ToDoItem) : TasksDb {
        return TasksDb(
            item.id,
            item.taskDescription,
            item.isCompleted,
            item.importance,
            item.creatingDate,
            item.deadlineDate,
            item.changingDate
        )
    }

    fun fromDatabaseElement(item: TasksDb) : ToDoItem {
        return ToDoItem(
            item.id,
            item.description,
            item.isCompleted,
            item.importance,
            item.creatingDate,
            item.deadlineDate,
            item.changingDate
        )
    }

    fun fromDatabaseList(items : List<TasksDb>) : List<ToDoItem> {
        val newList: MutableList<ToDoItem> = mutableListOf()

        for (item in items) {
            newList += fromDatabaseElement(item)
        }

        return newList
    }

    fun toDatabaseList(items: List<ToDoItem>) : List<TasksDb> {
        val newList: MutableList<TasksDb> = mutableListOf()

        for (item in items) {
            newList += toDatabaseElement(item)
        }

        return newList
    }

    // TODO: прокинуть айди
    private fun toElement(item: ToDoItem): ElementDto = ElementDto(
        item.id,
        item.taskDescription,
        item.importance.toDtoName(),
        item.deadlineDate,
        item.isCompleted,
        null,
        item.creatingDate,
        item.changingDate ?: System.currentTimeMillis(),
        "me"
    )
}