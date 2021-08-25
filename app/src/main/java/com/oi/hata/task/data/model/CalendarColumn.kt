package com.oi.hata.task.data.model

data class CalendarColumn(
    var important: Int,
    var completed: Int,
    var due: Int,
    var tasks: MutableList<CalendarTaskItem>
)

