package com.oi.hata.common.reminder.data.local.model

data class HataReminder(
    val reminderId: Long,
    val reminderTime: String,
    val reminderMonths: List<String>?,
    val reminderDates: List<Int>?,
    val reminderWeeks: List<String>?,
    val reminderWeekNum: List<Int>?,
    val alarmScreenVal: String,
    val remCustomWhenSelectType: String,
    val reminderFormat: String,
    val reminderEndYear: Int,
    val reminderOption: String,
    val remoptPickDate: String
)
