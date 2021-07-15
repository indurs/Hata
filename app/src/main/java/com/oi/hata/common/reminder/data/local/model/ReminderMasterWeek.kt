package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReminderMasterWeek(
    @Embedded
    val reminderMaster: ReminderMaster,
    @Relation(
        parentColumn = "reminder_id",
        entityColumn = "week_reminder_id"
    )
    val reminderWeeks: List<ReminderWeek>
)
