package com.oi.hata.common.reminder.data.local.datasource

import androidx.room.withTransaction
import com.oi.hata.common.reminder.data.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataLocalReminderDatasource @Inject constructor(
    private val hataDatabase: HataDatabase,
    private val reminderDao: ReminderDao,
    private val taskDao: TaskDao
): HataReminderDatasource {


    override suspend fun insertTaskReminder(
        hataReminder: HataReminder?,
        task: Task
    ) {
        hataDatabase.withTransaction {
            val reminderId = reminderDao.insertReminder(hataReminder = hataReminder)
            task.taskReminderId = reminderId
            taskDao.insertTask(task)
        }
    }

    override suspend fun updateTaskReminder(
        hataReminder: HataReminder?,
        task: Task
    ) {
        hataDatabase.withTransaction {
            taskDao.deleteTask(task)
            val reminderId = reminderDao.insertReminder(hataReminder = hataReminder)
            reminderId?.let {
                task.taskReminderId = reminderId
            }
            taskDao.insertTask(task)
        }
    }

    override suspend fun getTasks(month: Int, date: Int):List<Task>  =
        reminderDao.getTasks(monthNum = month, date = date)


}