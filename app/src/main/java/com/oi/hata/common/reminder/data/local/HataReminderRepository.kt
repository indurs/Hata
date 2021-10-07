package com.oi.hata.common.reminder.data.local

import com.oi.hata.common.reminder.data.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataReminderRepository @Inject constructor(
   private val hataReminderDatasource: HataReminderDatasource
): IHataReminderRepository {
    override suspend fun insertTaskReminder(hataReminder: HataReminder?, task: Task) {
        hataReminderDatasource.insertTaskReminder(hataReminder,task)
    }

    override suspend fun updateTaskReminder(hataReminder: HataReminder?, task: Task) {
        hataReminderDatasource.updateTaskReminder(hataReminder,task)
    }

}