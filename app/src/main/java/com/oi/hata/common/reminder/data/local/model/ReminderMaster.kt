package com.oi.hata.common.reminder.data.local.model

import androidx.room.*
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity(tableName = "reminder_master")
data class ReminderMaster(
    @ColumnInfo(name = "reminder_id") @PrimaryKey (autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "reminder_text") var reminderText: String,
    @ColumnInfo(name = "reminder_format") var reminderFormat: String?,
    @ColumnInfo(name = "alarm_screen_val") var alarmScreenVal: String,
    @ColumnInfo(name = "reminder_time") var reminderTime: String,
    @ColumnInfo(name = "task_due_date") var taskDueDate: LocalDate,
    @ColumnInfo(name = "remopt_pick_date") var remoptPickDate: String,
    @ColumnInfo(name = "reminder_end_year") var reminderEndyear: Int,
    @ColumnInfo(name = "reminder_option") var reminderOption: String,
    @ColumnInfo(name = "reminder_custom_when_type") var remCustomWhenSelectType: String,
)



