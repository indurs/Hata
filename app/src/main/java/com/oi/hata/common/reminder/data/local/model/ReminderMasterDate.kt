package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded

data class ReminderMasterDate(
    @Embedded
    val reminderMaster: ReminderMaster,
    @Embedded
    val reminderDate: ReminderDate
)
