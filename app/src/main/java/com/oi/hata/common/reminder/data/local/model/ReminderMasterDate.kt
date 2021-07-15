package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReminderMasterDate(
    @Embedded
    val reminderMaster: ReminderMaster,
    @Relation(
        parentColumn = "reminder_id",
        entityColumn = "date_reminder_id"
    )
    val reminderDates: List<ReminderDate>
)
