package com.oi.hata.task.data.model

import java.time.OffsetDateTime

data class CalendarTaskItem(
    var task_id: Long = 0,
    var task: String,
    var reminder: Boolean?,
    var task_due_date: Int?,
    var reminder_date: Int? = 0,
    var reminder_week: String? = "",
    var important_group_id: Long,
    var reminder_weeknum: Int?,
    var completed: Boolean,
    var task_create_date: OffsetDateTime
)
