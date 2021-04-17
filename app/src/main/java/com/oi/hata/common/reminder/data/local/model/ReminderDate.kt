package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(
    tableName = "reminder_date",
    foreignKeys = [
    ForeignKey(
        entity = ReminderMaster::class,
        parentColumns = ["reminder_id"],
        childColumns = ["date_reminder_id"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["date_reminder_id"])]
)
data class ReminderDate(
    @ColumnInfo(name = "date_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "date_reminder_id") var dateReminderId: Long,
    @ColumnInfo(name = "reminder_date") var reminderDate: Int
)
