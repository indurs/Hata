package com.oi.hata.common.reminder.data.local.model

import androidx.room.Embedded

data class ReminderMasterWeeknum(
    @Embedded
    val reminderMaster: ReminderMaster,
    val reminderWeekNum: ReminderWeekNum
)
