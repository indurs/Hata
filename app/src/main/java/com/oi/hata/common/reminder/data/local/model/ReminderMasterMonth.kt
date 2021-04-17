package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded

data class ReminderMasterMonth(
    @Embedded
    val reminderMaster: ReminderMaster,
    val reminderMonth: ReminderMonth
)
