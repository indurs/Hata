package com.oi.hata.common.reminder.data.local.dao

import androidx.room.*
import com.oi.hata.common.reminder.data.local.model.*
import com.oi.hata.common.reminder.ui.ReminderViewModel

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderMaster(reminderMaster: ReminderMaster):Long

    @Update
    suspend fun updateReminderMaster(reminderMaster: ReminderMaster)

    @Delete
    suspend fun deleteReminderMaster(reminderMaster: ReminderMaster)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderDate(reminderDate: ReminderDate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReminderDates(reminderDates: List<ReminderDate>)

    @Update
    suspend fun updateReminderDate(reminderDate: ReminderDate)

    @Delete
    suspend fun deleteReminderDate(reminderDate: ReminderDate)




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderMonth(reminderMonth: ReminderMonth)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReminderMonths(reminderMonths: List<ReminderMonth>)

    @Update
    suspend fun updateReminderDate(reminderMonth: ReminderMonth)

    @Delete
    suspend fun deleteReminderMonth(reminderMonth: ReminderMonth)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderWeek(reminderWeek: ReminderWeek)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReminderWeeks(reminderWeeks: List<ReminderWeek>)

    @Update
    suspend fun updateReminderWeek(reminderWeek: ReminderWeek)

    @Delete
    suspend fun deleteReminderWeek(reminderWeek: ReminderWeek)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderWeekNum(reminderWeekNum: ReminderWeekNum)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReminderWeekNums(reminderWeekNum: List<ReminderWeekNum>)

    @Update
    suspend fun updateReminderWeekNum(reminderWeekNum: ReminderWeekNum)

    @Delete
    suspend fun deleteReminderWeekNum(reminderWeekNum: ReminderWeekNum)

    @Transaction
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

    ){
        var reminderMaster = ReminderMaster(reminderText = reminderTxt,
                                            alarmScreenVal = alarmScreenVal,
                                            reminderTime = reminderTime,
                                            reminderDueDate = reminderDueDate,
                                            reminderDueMonth = reminderDueMonth,
                                            reminderDueYear = reminderDueYear,
                                            whenSelectType = whenSelectType,
                                            reminderFormat = "",
                                            reminderPickDate = "",
                                            reminderPickMonth = "",
                                            reminderPickYear = ""
                                            )



        var reminderId = insertReminderMaster(reminderMaster = reminderMaster)

        when(whenSelectType){

            ReminderViewModel.ChoosenType.DATE.name -> {
                insertAllReminderDates(reminderDates = transformDates(reminderId = reminderId,reminderDates = reminderDates))
            }
            ReminderViewModel.ChoosenType.MONTH.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = reminderMonths))
            }
            ReminderViewModel.ChoosenType.MONTHDATE.name -> {
                insertAllReminderDates(reminderDates = transformDates(reminderId = reminderId,reminderDates = reminderDates))
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = reminderMonths))
            }

            ReminderViewModel.ChoosenType.MONTHWEEK.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = reminderMonths))
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = reminderWeeks))
            }

            ReminderViewModel.ChoosenType.MONTHWEEKNUM.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = reminderMonths))
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = reminderWeeks))
                insertAllReminderWeekNums(transformWeekNums(reminderId = reminderId,reminderWeekNums = reminderWeekNum))
            }
            ReminderViewModel.ChoosenType.WEEK.name -> {
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = reminderWeeks))
            }
            ReminderViewModel.ChoosenType.WEEKNUM.name -> {
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = reminderWeeks))
                insertAllReminderWeekNums(transformWeekNums(reminderId = reminderId,reminderWeekNums = reminderWeekNum))
            }

        }

    }

    fun transformMonths( reminderId: Long, reminderMonths: List<String>): List<ReminderMonth>{
        return reminderMonths.map {
            ReminderMonth(monthReminderId = reminderId, reminderMonth = it)
        }

    }

    fun transformDates( reminderId: Long, reminderDates: List<Int>): List<ReminderDate>{
        return reminderDates.map {
            ReminderDate(dateReminderId = reminderId, reminderDate = it)
        }

    }

    fun transformWeeks( reminderId: Long, reminderWeeks: List<String>): List<ReminderWeek>{
        return reminderWeeks.map {
            ReminderWeek(weekReminderId = reminderId, reminderWeek = it)
        }

    }

    fun transformWeekNums( reminderId: Long, reminderWeekNums: List<Int>): List<ReminderWeekNum>{
        return reminderWeekNums.map {
            ReminderWeekNum(weekNumReminderId = reminderId, reminderWeekNum = it)
        }
    }

}