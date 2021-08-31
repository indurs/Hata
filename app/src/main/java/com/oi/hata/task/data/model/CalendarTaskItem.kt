package com.oi.hata.task.data.model

data class CalendarTaskItem(
    var task_id: Long = 0,
    var task: String,
    var reminder: Boolean?,
    var task_due_date: Int?,
    var reminder_date: Int? = 0,
    var reminder_week: String? = "",
    var important_group_id: Long,
    var reminder_weeknum: Int?,
    var completed: Boolean
)
