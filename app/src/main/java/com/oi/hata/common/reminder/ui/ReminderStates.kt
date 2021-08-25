package com.oi.hata.common.reminder.ui

import androidx.compose.runtime.Stable

@Stable
interface ReminderState{
    var reminderTimeSelected: Boolean
    var reminderTime: String
    var reminderSelected: Boolean
    var reminderOptSelected: String
    var pickAdate: String
    var pickDateSelectedL: Boolean

    val onTimeSelect: (hour:Int, minute:Int, am:Boolean) -> Unit
    val resetReminder: () -> Unit
    val initReminderValues: () -> Unit
    val onClearReminderValues: () -> Unit
}

@Stable
interface CustomReminderState{
    val onCustomReminderSelect: () -> Unit
    val onCustomReminderInitialize: () -> Unit
    val onCompleteReminder: () -> Unit
    val onCloseReminder: () -> Unit
    val customreminder: String
}