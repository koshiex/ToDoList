package com.kionavani.todotask.data

import com.kionavani.todotask.data.remote.dto.ElementDto
import com.kionavani.todotask.data.remote.dto.ResponseDto.*
import com.kionavani.todotask.data.remote.dto.RequestDto.*

/**
 * Мапит данные из DTO сетевых ответов/запросов в модель UI-ая
 */
class TasksMapper {
    fun toEntityList(dto: ListElementResponseDto): List<ToDoItem> {
        val mapResult: MutableList<ToDoItem> = mutableListOf()

        for (element in dto.list) {
            mapResult += toEntity(element)
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

    fun toEntity(element: ElementDto): ToDoItem = ToDoItem(
        element.id,
        element.text,
        element.done,
        element.importance.toImportance(),
        element.created_at,
        element.deadline,
        element.changed_at
    )

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