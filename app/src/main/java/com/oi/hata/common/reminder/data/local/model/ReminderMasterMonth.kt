package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReminderMasterMonth(
    @Embedded
    val reminderMaster: ReminderMaster,

    @Relation(
        parentColumn = "reminder_id",
        entityColumn = "month_reminder_id"
    )
    val reminderMonths: List<ReminderMonth>
)
