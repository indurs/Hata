package com.oi.hata.common.reminder.data.local.datasource

import androidx.room.withTransaction
import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.ImportantGroupTask
import com.oi.hata.task.data.model.Task
import com.oi.hata.task.data.model.TaskUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataReminderDatasource @Inject constructor(private val hataDatabase: HataDatabase,
                                                 private val reminderDao: ReminderDao,
                                                 private val groupDao: GroupDao,
                                                 private val taskDao: TaskDao){

    suspend fun insertReminder(
                            hataReminder: HataReminder

    ) {

        reminderDao.insertReminder(
                           hataReminder = hataReminder
        )
    }

    suspend fun insertTask(task: Task){
        taskDao.insertTask(task)
    }


    suspend fun insertTaskReminder(
        hataReminder: HataReminder,
        task: Task
    ) {
          println("insertTaskReminder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+task.taskGroupId)
          hataDatabase.withTransaction {
                val reminderId = reminderDao.insertReminder(hataReminder = hataReminder)
                task.taskReminderId = reminderId
                taskDao.insertTask(task)
          }
    }

    suspend fun updateTaskReminder(
        hataReminder: HataReminder,
        task: Task
    ){
        hataDatabase.withTransaction {
            taskDao.deleteTask(task)
            val reminderId = reminderDao.insertReminder(hataReminder = hataReminder)
            task.taskReminderId = reminderId
            taskDao.insertTask(task)
        }
    }


    suspend fun getTodaysReminders(): Flow<List<Task>> =  reminderDao.getTodaysReminders()

    suspend fun getTask(taskId: Long): Flow<TaskUIState> = flow{

        var task: Task?
        var taskUIState = TaskUIState(null,null)

        hataDatabase.withTransaction {
            task = taskDao.getTask(taskId)
            if(task!=null){
                taskUIState.task = task
                reminderDao.getReminder(task!!.taskReminderId).collect {
                    taskUIState.hataReminder = it
                }
            }
        }

        emit(taskUIState)

    }

    fun getGroupTask(groupName:String): Flow<GroupTask> = groupDao.getTaskGroup(groupName)

    fun getImportantGroupTask(groupId:Long): Flow<ImportantGroupTask> = groupDao.getImportantGroup(groupId)

    fun getGroupTasks(): Flow<List<GroupTask>> = groupDao.getTaskGroups()

    suspend fun getReminder(reminderId: Long): Flow<HataReminder> = reminderDao.getReminder(reminderId)

}