package com.oi.hata.task.data

import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HataTaskDatasource @Inject constructor(private val hataDatabase: HataDatabase,
                                             private val taskDao: TaskDao,
                                             private val groupDao: GroupDao
){

    suspend fun insertGroup(group: Group){
        hataDatabase.groupDao().insertGroup(group)
    }

    suspend fun getTaskGroups(): Flow<List<GroupTask>> {
        return hataDatabase.groupDao().getTaskGroups()
    }

}