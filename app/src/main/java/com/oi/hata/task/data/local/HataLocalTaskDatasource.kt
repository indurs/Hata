package com.oi.hata.task.data.local

import androidx.room.withTransaction
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.task.data.HataTaskDatasource
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataLocalTaskDatasource @Inject constructor(
    private val hataDatabase: HataDatabase,
    private val taskDao: TaskDao,
    private val groupDao: GroupDao,
    private val reminderDao: ReminderDao
) : HataTaskDatasource {

    override suspend fun insertGroup(group: Group): Long {
        return hataDatabase.groupDao().insertGroup(group)
    }

    override suspend fun deleteTask(task: Task) {
        hataDatabase.taskDao().deleteTask(task)
    }

    override suspend fun getTaskGroups(): Flow<List<GroupTask>> {
        return hataDatabase.groupDao().getTaskGroups()
    }

    override suspend fun updateTask(
        task: Task
    ) {
        hataDatabase.withTransaction {
            taskDao.deleteTask(task)
            taskDao.insertTask(task)
        }
    }

    override suspend fun getTasksForMonth(month: Int): Flow<TreeMap<Int, CalendarColumn>> = flow {
        reminderDao.getTasksForMonth(month).collect {
            emit(it)
        }
    }

    override suspend fun getTasks(month: Int, date: Int):List<Task> {
        var tasks = mutableListOf<Task>()
        tasks.addAll(taskDao.getDueTasksForDay(month, date, GregorianCalendar().get(Calendar.YEAR)))

        var calendar = GregorianCalendar()
        if(calendar.get(Calendar.DAY_OF_MONTH) == date){
            tasks.addAll(taskDao.getTodayTasks(true))
        }

        return tasks
    }

    override suspend fun getTask(taskId: Long): Flow<TaskUIState> = flow {
        var task: Task?
        var taskUIState = TaskUIState(null, null)

        hataDatabase.withTransaction {
            task = taskDao.getTask(taskId)
            if (task != null) {
                taskUIState.task = task
                if (task!!.taskReminderId != null) {
                    reminderDao.getReminder(task!!.taskReminderId!!).collect {
                        taskUIState.hataReminder = it
                    }
                }
            }
        }

        emit(taskUIState)

    }

    override suspend fun getTasksForGroup(groupName: String): Flow<GroupTask> =
        groupDao.getTasksForGroup(groupName)

    override suspend fun getImportantTasksCount(): Flow<Int> = taskDao.getImportantTasksCount()

    override suspend fun getGroupTasks(): Flow<List<GroupTask>> = groupDao.getTaskGroups()

}


