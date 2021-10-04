package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(
    tableName = "reminder_weeknum",
    foreignKeys = [
        ForeignKey(
            entity = ReminderMaster::class,
            parentColumns = ["reminder_id"],
            childColumns = ["weeknum_reminder_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["weeknum_reminder_id"])]
)
data class ReminderWeekNum(
    @ColumnInfo(name = "weeknum_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "weeknum_reminder_id") var weekNumReminderId: Long,
    @ColumnInfo(name = "reminder_weeknum") var reminderWeekNum: Int
)
