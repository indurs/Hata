package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded

data class ReminderMasterWeek(
    @Embedded
    val reminderMaster: ReminderMaster,
    val reminderWeek: ReminderWeek
)
