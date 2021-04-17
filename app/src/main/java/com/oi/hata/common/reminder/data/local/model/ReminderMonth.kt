package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(
    tableName = "reminder_month",
    foreignKeys = [
    ForeignKey(
        entity = ReminderMaster::class,
        parentColumns = ["reminder_id"],
        childColumns = ["month_reminder_id"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["month_reminder_id"])])
data class ReminderMonth(
    @ColumnInfo(name = "month_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "month_reminder_id") var monthReminderId: Long,
    @ColumnInfo(name = "reminder_month") var reminderMonth: String)
