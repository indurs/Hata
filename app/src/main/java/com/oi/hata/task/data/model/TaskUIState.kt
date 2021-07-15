package com.oi.hata.task.data.model

import com.oi.hata.common.reminder.data.local.model.HataReminder

data class TaskUIState(
    var hataReminder: HataReminder?,
    var task: Task?
)
