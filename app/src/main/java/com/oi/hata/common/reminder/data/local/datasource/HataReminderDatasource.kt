package com.oi.hata.common.reminder.data.local.datasource

import com.oi.hata.common.reminder.data.local.HataDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.ui.ReminderViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HataReminderDatasource @Inject constructor(private val reminderDao: ReminderDao){

    suspend fun insertReminder(reminderTxt: String,
                       alarmScreenVal: String,
                       reminderTime: String,
                       reminderDueDate: String,
                       reminderDueMonth: String,
                       reminderDueYear: String,
                       whenSelectType: String,
                       reminderMonths: List<String>,
                       reminderDates: List<Int>,
                       reminderWeeks: List<String>,
                       reminderWeekNum: List<Int>

    ) {

       reminderDao.insertReminder(
            reminderTxt,
            alarmScreenVal, reminderTime, reminderDueDate,
            reminderDueMonth, reminderDueYear, whenSelectType,
            reminderMonths, reminderDates, reminderWeeks, reminderWeekNum
        )


    }
}