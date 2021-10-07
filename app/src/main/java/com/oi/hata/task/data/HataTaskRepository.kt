package com.oi.hata.task.data

import com.oi.hata.common.reminder.data.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.datasource.HataLocalReminderDatasource
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataTaskRepository @Inject constructor(
    private val hataLocalTaskDatasource: HataTaskDatasource,
    private val hataLocalReminderDatasource: HataReminderDatasource
) : IHataTaskRepository {

    override suspend fun insertGroup(group: Group): Long {
        return hataLocalTaskDatasource.insertGroup(group)
    }

    override suspend fun deleteTask(task: Task) {
        hataLocalTaskDatasource.deleteTask(task)
    }

    override suspend fun getTaskGroups(): Flow<List<GroupTask>> =
        hataLocalTaskDatasource.getTaskGroups()


    override suspend fun updateTask(task: Task) {
        hataLocalTaskDatasource.updateTask(task)
    }

    override suspend fun getTasksForMonth(month: Int): Flow<TreeMap<Int, CalendarColumn>> =
        hataLocalTaskDatasource.getTasksForMonth(month)

    override suspend fun getTasks(month: Int, date: Int): Flow<List<Task>> = flow {

        var tasks = mutableListOf<Task>()

        tasks.addAll(hataLocalTaskDatasource.getTasks(month, date))
        tasks.addAll(hataLocalReminderDatasource.getTasks(month, date))

        emit(tasks)
    }


    override suspend fun getTasks(whenType: String): Flow<List<Task>> = flow {
        var calendar = GregorianCalendar()

        if (whenType == ReminderUtil.TOMORROW)
            calendar.add(Calendar.DAY_OF_MONTH, 1)

        var date = calendar.get(Calendar.DAY_OF_MONTH)

        var tasks = mutableListOf<Task>()

        tasks.addAll(hataLocalTaskDatasource.getTasks(calendar.get(Calendar.MONTH) + 1, date))
        tasks.addAll(hataLocalReminderDatasource.getTasks(calendar.get(Calendar.MONTH) + 1, date))

        emit(tasks)

    }

    override suspend fun getTask(taskId: Long): Flow<TaskUIState> =
        hataLocalTaskDatasource.getTask(taskId)


    override suspend fun getTasksForGroup(groupName: String): Flow<GroupTask> =
        hataLocalTaskDatasource.getTasksForGroup(groupName)


    override suspend fun getImportantTasksCount(): Flow<Int> =
        hataLocalTaskDatasource.getImportantTasksCount()


    override suspend fun getGroupTasks(): Flow<List<GroupTask>> =
        hataLocalTaskDatasource.getGroupTasks()

}

