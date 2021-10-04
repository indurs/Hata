package com.oi.hata.task.data

import com.oi.hata.task.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*

interface IHataTaskRepository {
    suspend fun insertGroup(group: Group):Long
    suspend fun deleteTask(task: Task)
    suspend fun getTaskGroups(): Flow<List<GroupTask>>
    suspend fun updateTask(task: Task)
    suspend fun getTasksForMonth(month: Int): Flow<TreeMap<Int, CalendarColumn>>
    suspend fun getTasks(month: Int,date:Int): Flow<List<Task>>
    suspend fun getTasks(whenType: String): Flow<List<Task>>
    suspend fun getTask(taskId: Long): Flow<TaskUIState>
    suspend fun getTasksForGroup(groupName: String): Flow<GroupTask>
    suspend fun getImportantTasksCount(): Flow<Int>
    suspend fun getGroupTasks(): Flow<List<GroupTask>>
}