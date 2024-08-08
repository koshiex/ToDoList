package com.kionavani.todotask

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.TasksMapper
import com.kionavani.todotask.data.TodoItemsRepositoryImpl
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.dto.ResponseDto.ListElementResponseDto
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.ui.dataStore
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [30], application = TestTodoApplication::class)
@RunWith(RobolectricTestRunner::class)
class TodoItemsRepositoryImplTest {
    private lateinit var repository: TodoItemsRepositoryImpl
    private lateinit var mockDatabase: AppDatabase
    private lateinit var dataStore: DataStore<Preferences>

    private val serviceImpl: TasksServiceImpl = mockk()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val tasksMapper = TasksMapper()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Context>()

        mockDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dataStore = context.dataStore

        repository = TodoItemsRepositoryImpl(
            networkService = serviceImpl,
            database = mockDatabase,
            dataStore = dataStore,
            tasksMapper = tasksMapper,
            coroutineScope = testScope,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        mockDatabase.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchData with empty local and server data`() = runTest {
        coEvery { serviceImpl.getList() } returns NetworkResult.Success(
            ListElementResponseDto(
                "ok",
                emptyList(),
                0
            )
        )

        repository.fetchData()

        assertEquals(emptyList<ToDoItem>(), repository.todoItems.value)
    }

    @Test
    fun `test fetchData with local data and empty server`() = runTest {
        val taskList = createTasksList()
        mockDatabase.tasksDao().insertTasksList(tasksMapper.toDatabaseList(taskList))

        coEvery { serviceImpl.getList() } returns NetworkResult.Success(
            ListElementResponseDto(
                "ok",
                emptyList(),
                0
            )
        )
        coEvery { serviceImpl.updateList( any(), any() ) } returns
                NetworkResult.Success(tasksMapper.toResponseList(taskList, 0, "ok"))

        repository.fetchData()

        assertEquals(taskList, repository.todoItems.value)
    }

    @Test
    fun `test fetchData offline`() = runTest {
        val taskList = createTasksList()
        mockDatabase.tasksDao().insertTasksList(tasksMapper.toDatabaseList(taskList))

        repository.changeNetworkStatus(false)
        repository.fetchData()

        assertEquals(taskList, repository.todoItems.value)
    }

    private fun createTasksList(): List<ToDoItem> =
        listOf(
            ToDoItem(
                id = "1",
                taskDescription = "Buy groceries",
                isCompleted = false,
                importance = Importance.HIGH,
                creatingDate = 1625232000000L,
                deadlineDate = 1625836800000L,
                changingDate = 1625318400000L
            ),
            ToDoItem(
                id = "2",
                taskDescription = "Finish project report",
                isCompleted = false,
                importance = Importance.HIGH,
                creatingDate = 1625318400000L,
                deadlineDate = 1626000000000L,
                changingDate = 1625404800000L
            ),
            ToDoItem(
                id = "3",
                taskDescription = "Call plumber",
                isCompleted = true,
                importance = Importance.REGULAR,
                creatingDate = 1625404800000L,
                changingDate = 1625491200000L
            ),
            ToDoItem(
                id = "4",
                taskDescription = "Schedule dentist appointment",
                isCompleted = false,
                importance = Importance.LOW,
                creatingDate = 1625491200000L,
                deadlineDate = 1626096000000L,
                changingDate = System.currentTimeMillis()
            ),
            ToDoItem(
                id = "5",
                taskDescription = "Plan weekend trip",
                isCompleted = true,
                importance = Importance.REGULAR,
                creatingDate = 1625577600000L,
                changingDate = 1625664000000L
            ),
            ToDoItem(
                id = "6",
                taskDescription = "Read a book",
                isCompleted = false,
                importance = Importance.LOW,
                creatingDate = 1625664000000L,
                changingDate = System.currentTimeMillis()
            ),
            ToDoItem(
                id = "7",
                taskDescription = "Workout session",
                isCompleted = true,
                importance = Importance.HIGH,
                creatingDate = 1625750400000L,
                changingDate = 1625836800000L
            ),
            ToDoItem(
                id = "8",
                taskDescription = "Attend webinar",
                isCompleted = false,
                importance = Importance.REGULAR,
                creatingDate = 1625836800000L,
                deadlineDate = 1626441600000L,
                changingDate = System.currentTimeMillis()
            ),
            ToDoItem(
                id = "9",
                taskDescription = "Update software",
                isCompleted = true,
                importance = Importance.LOW,
                creatingDate = 1625923200000L,
                changingDate = 1626009600000L
            ),
            ToDoItem(
                id = "10",
                taskDescription = "Clean garage",
                isCompleted = false,
                importance = Importance.REGULAR,
                creatingDate = 1626009600000L,
                deadlineDate = 1626614400000L,
                changingDate = System.currentTimeMillis()
            )
        )

}
