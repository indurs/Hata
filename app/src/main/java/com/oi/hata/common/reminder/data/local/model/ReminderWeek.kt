package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(
    tableName = "reminder_week",
    foreignKeys = [
    ForeignKey(
        entity = ReminderMaster::class,
        parentColumns = ["reminder_id"],
        childColumns = ["week_reminder_id"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["week_reminder_id"])])
data class ReminderWeek(
    @ColumnInfo(name = "reminder_week_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "week_reminder_id") var weekReminderId: Long,
    @ColumnInfo(name = "reminder_week") var reminderWeek: String
)
