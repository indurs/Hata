package com.oi.hata.task.data

import androidx.room.withTransaction
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataTaskDatasource @Inject constructor(private val hataDatabase: HataDatabase,
                                             private val taskDao: TaskDao,
                                             private val groupDao: GroupDao,
                                             private val reminderDao: ReminderDao
){

    suspend fun insertGroup(group: Group):Long{
        return hataDatabase.groupDao().insertGroup(group)
    }

    suspend fun deleteTask(task: Task){
        hataDatabase.taskDao().deleteTask(task)
    }

    suspend fun getTaskGroups(): Flow<List<GroupTask>> {
        return hataDatabase.groupDao().getTaskGroups()
    }

    suspend fun updateTask(
        task: Task
    ){
        println(" updateTask TASK GROUP >>>>>>importantGroupId "+task.importantGroupId + " " + task.todaytask)
        hataDatabase.withTransaction {
            taskDao.deleteTask(task)
            taskDao.insertTask(task)
        }
    }

    suspend fun getTasksForMonth(month: Int):Flow<TreeMap<Int, CalendarColumn>> = reminderDao.getTasksForMonth(month)

    suspend fun getTasksForCalendarDate(month: Int,date:Int) = reminderDao.getTasksForCalendarDate(month = month,date = date)

}