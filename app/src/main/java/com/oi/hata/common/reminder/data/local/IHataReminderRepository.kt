package com.oi.hata.common.reminder.data.local

import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow

interface IHataReminderRepository {
    suspend fun insertTaskReminder(
        hataReminder: HataReminder?,
        task: Task
    )

    suspend fun updateTaskReminder(
        hataReminder: HataReminder,
        task: Task
    )
}