package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(tableName = "reminder_master")
data class ReminderMaster(
    @ColumnInfo(name = "reminder_id") @PrimaryKey (autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "reminder_text") var reminderText: String,
    @ColumnInfo(name = "reminder_format") var reminderFormat: String?,
    @ColumnInfo(name = "alarm_screen_val") var alarmScreenVal: String,
    @ColumnInfo(name = "reminder_time") var reminderTime: String,
    @ColumnInfo(name = "reminder_due_date") var reminderDueDate: String,
    @ColumnInfo(name = "reminder_due_month") var reminderDueMonth: String,
    @ColumnInfo(name = "reminder_due_year") var reminderDueYear: String,
    @ColumnInfo(name = "reminder_pick_date") var reminderPickDate: String?,
    @ColumnInfo(name = "reminder_pick_month") var reminderPickMonth: String?,
    @ColumnInfo(name = "reminder_pick_year") var reminderPickYear: String?,
    @ColumnInfo(name = "when_select_type") var whenSelectType: String,

)


