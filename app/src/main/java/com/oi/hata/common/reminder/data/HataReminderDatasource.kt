package com.oi.hata.common.reminder.data

import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.task.data.model.Task
import com.oi.hata.task.data.model.TaskUIState
import kotlinx.coroutines.flow.Flow

interface HataReminderDatasource {
    suspend fun insertTaskReminder(
        hataReminder: HataReminder?,
        task: Task
    )

    suspend fun updateTaskReminder(
        hataReminder: HataReminder,
        task: Task
    )

    suspend fun getTasks(month: Int, date: Int):List<Task>

}