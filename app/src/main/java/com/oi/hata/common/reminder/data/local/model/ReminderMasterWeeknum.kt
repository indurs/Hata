package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReminderMasterWeeknum(
    @Embedded
    val reminderMaster: ReminderMaster,
    @Relation(
        parentColumn = "reminder_id",
        entityColumn = "weeknum_reminder_id"
    )
    val reminderWeekNums: List<ReminderWeekNum>

)
