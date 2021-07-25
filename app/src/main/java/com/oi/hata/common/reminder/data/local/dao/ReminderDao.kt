package com.oi.hata.common.reminder.data.local.dao

import android.util.Log
import androidx.annotation.TransitionRes
import androidx.room.*
import com.oi.hata.common.reminder.data.local.model.*
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.ArrayList

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
    suspend fun insertReminder(
                                hataReminder: HataReminder
                                /*reminderTxt: String,
                                alarmScreenVal: String,
                                reminderOption: String,
                                reminderTime: String,
                                reminderDueDate: LocalDate,
                                reminderEndYear: Int,
                                remCustomWhenSelectType: String,
                                reminderMonths: List<String>,
                                reminderDates: List<Int>,
                                reminderWeeks: List<String>,
                                reminderWeekNum: List<Int>,
                                remoptPickDate: String*/

    ): Long{
        var reminderMaster = ReminderMaster(
                                            alarmScreenVal = hataReminder.alarmScreenVal,
                                            reminderTime = hataReminder.reminderTime,
                                            remCustomWhenSelectType = hataReminder.remCustomWhenSelectType,
                                            reminderFormat = "",
                                            reminderEndyear = hataReminder.reminderEndYear,
                                            reminderOption = hataReminder.reminderOption,
                                            remoptPickDate = hataReminder.remoptPickDate
                                            )

        var reminderId = insertReminderMaster(reminderMaster = reminderMaster)


        when(hataReminder.remCustomWhenSelectType){

            ReminderUtil.WhenSelectType.DATE.name -> {
                insertAllReminderDates(reminderDates = transformDates(reminderId = reminderId,reminderDates = hataReminder.reminderDates!!))
            }
            ReminderUtil.WhenSelectType.MONTH.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = hataReminder.reminderMonths!!))
            }
            ReminderUtil.WhenSelectType.MONTHDATE.name -> {
                insertAllReminderDates(reminderDates = transformDates(reminderId = reminderId,reminderDates = hataReminder.reminderDates!!))
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = hataReminder.reminderMonths!!))
                Log.d("INSERT ", "MONTHDATE "+ hataReminder.reminderMonths[0] +" date "+hataReminder.reminderDates[0])
            }

            ReminderUtil.WhenSelectType.MONTHWEEK.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = hataReminder.reminderMonths!!))
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = hataReminder.reminderWeeks!!))
            }

            ReminderUtil.WhenSelectType.MONTHWEEKNUM.name -> {
                insertAllReminderMonths(reminderMonths = transformMonths(reminderId = reminderId,reminderMonths = hataReminder.reminderMonths!!))
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = hataReminder.reminderWeeks!!))
                insertAllReminderWeekNums(transformWeekNums(reminderId = reminderId,reminderWeekNums = hataReminder.reminderWeekNum!!))
            }
            ReminderUtil.WhenSelectType.WEEK.name -> {
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = hataReminder.reminderWeeks!!))
            }
            ReminderUtil.WhenSelectType.WEEKNUM.name -> {
                insertAllReminderWeeks(transformWeeks(reminderId = reminderId,reminderWeeks = hataReminder.reminderWeeks!!))
                insertAllReminderWeekNums(transformWeekNums(reminderId = reminderId,reminderWeekNums = hataReminder.reminderWeekNum!!))
            }

        }
        return reminderId
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

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master INNER JOIN task ON reminder_id = task_reminder_id WHERE reminder_custom_when_type = :whenSelectType """)
    fun getAllReminders(whenSelectType: String): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master INNER JOIN task ON reminder_id = task_reminder_id INNER JOIN reminder_date ON reminder_id = date_reminder_id where reminder_custom_when_type = :whenSelectType AND reminder_date = :date """)
    fun getRemindersforDate(whenSelectType: String,date: Int): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master INNER JOIN task ON reminder_id = task_reminder_id INNER JOIN  reminder_month ON reminder_id = month_reminder_id WHERE reminder_custom_when_type = :whenSelectType AND reminder_month = :month """)
    fun getRemindersforMonth(whenSelectType: String,month: String): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master
                        INNER JOIN task ON reminder_id = task_reminder_id
                        INNER JOIN reminder_month ON reminder_id = month_reminder_id 
                        INNER JOIN reminder_date ON reminder_id = date_reminder_id WHERE reminder_custom_when_type = :whenSelectType AND reminder_date = :date AND reminder_month = :month """)
    fun getRemindersforMonthDate(whenSelectType: String,month: String, date: Int): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master
                        INNER JOIN task ON reminder_id = task_reminder_id
                        INNER JOIN reminder_week ON reminder_id = week_reminder_id WHERE reminder_custom_when_type = :whenSelectType AND reminder_week = :week """)
    fun getRemindersforWeek(whenSelectType: String,week: String): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master
                        INNER JOIN task ON reminder_id = task_reminder_id
                        INNER JOIN reminder_week ON reminder_id = week_reminder_id 
                        INNER JOIN reminder_weeknum ON week_reminder_id
                        WHERE reminder_custom_when_type = :whenSelectType AND reminder_week = :week
                        AND reminder_weeknum = :weekNum """)
    fun getRemindersforWeekWeekNum(whenSelectType: String,week: String, weekNum: Int): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master
                        INNER JOIN task ON reminder_id = task_reminder_id
                        INNER JOIN reminder_month ON reminder_id = month_reminder_id
                        INNER JOIN reminder_week ON reminder_id = week_reminder_id
                        WHERE reminder_custom_when_type = :whenSelectType 
                        AND reminder_month = :month
                        AND reminder_week = :week
                        """)
    fun getRemindersforMonthWeek(whenSelectType: String,month: String, week: String): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,important_group_id,completed FROM reminder_master
                        INNER JOIN task ON reminder_id = task_reminder_id
                        INNER JOIN reminder_month ON reminder_id = month_reminder_id
                        INNER JOIN reminder_week ON reminder_id = week_reminder_id 
                        INNER JOIN reminder_weeknum ON week_reminder_id
                        WHERE reminder_custom_when_type = :whenSelectType 
                        AND reminder_month = :month
                        AND reminder_week = :week
                        AND reminder_weeknum = :weekNum """)
    fun getRemindersforMonthWeekWeekNum(whenSelectType: String,month: String, week: String, weekNum: Int): List<Task>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    suspend fun getTodaysReminders(): Flow<List<Task>> = flow{

        var todaysReminders = mutableListOf<Task>()

        var calendar = GregorianCalendar()
        var date = calendar.get(Calendar.DAY_OF_MONTH)
        var month = ReminderUtil.monthsStr[calendar.get(Calendar.MONTH)+1]

        var monthCalendar = ReminderUtil.getMonthCalendar(calendar.get(Calendar.MONTH)+1)
        //Log.d("DAte month ", " "+date + " " + month)

        var weekDay = ReminderUtil.weekDay(calendar.get(Calendar.MONTH)+1,date,calendar.get(Calendar.YEAR))
        var weekDayName = ReminderUtil.WEEKNAMES.values()[weekDay].name

        //println("weekday "+weekDay +"weekdayname "+weekDayName)

        var weekDays = monthCalendar.get(weekDayName)

        /*println("getRemindersforMonthDate "+ getRemindersforMonthDate(ReminderUtil.WhenSelectType.MONTHDATE.name ,month!!,date))
        println("getAllReminders(ReminderUtil.WhenSelectType.EVERYDAY.name) "+getAllReminders(ReminderUtil.EVERYDAY))
        println("getRemindersforDate( ReminderUtil.WhenSelectType.DATE.name,date) "+ getRemindersforDate( ReminderUtil.WhenSelectType.DATE.name,date))
        println("getRemindersforMonth( ReminderUtil.WhenSelectType.MONTH.name,month!!) "+getRemindersforMonth( ReminderUtil.WhenSelectType.MONTH.name,month!!))
        println(" getRemindersforMonthDate( ReminderUtil.WhenSelectType.MONTHDATE.name,month,date "+getRemindersforMonthDate( ReminderUtil.WhenSelectType.MONTHDATE.name,month,date))
        println("getRemindersforWeek( ReminderUtil.WhenSelectType.WEEK.name,weekDayName) "+getRemindersforWeek( ReminderUtil.WhenSelectType.WEEK.name,weekDayName))
        println("getRemindersforMonthWeek( ReminderUtil.WhenSelectType.MONTHWEEK.name,month,weekDayName) "+getRemindersforMonthWeek( ReminderUtil.WhenSelectType.MONTHWEEK.name,month,weekDayName))*/

        todaysReminders.addAll(getRemindersforMonthDate(ReminderUtil.WhenSelectType.MONTHDATE.name ,month!!,date))
        todaysReminders.addAll(getAllReminders(ReminderUtil.EVERYDAY))
        todaysReminders.addAll(getRemindersforDate( ReminderUtil.WhenSelectType.DATE.name,date))
        todaysReminders.addAll(getRemindersforMonth( ReminderUtil.WhenSelectType.MONTH.name,month!!))
        //todaysReminders.addAll(getRemindersforMonthDate( ReminderUtil.WhenSelectType.MONTHDATE.name,month,date))
        todaysReminders.addAll( getRemindersforWeek( ReminderUtil.WhenSelectType.WEEK.name,weekDayName))
        todaysReminders.addAll(getRemindersforMonthWeek( ReminderUtil.WhenSelectType.MONTHWEEK.name,month,weekDayName))

        for(i in 0 until weekDays!!.size){
            //println("week day "+weekDays[i] )
            if(weekDays[i] == date){
                /*println(">>>>>>>>>>>>>>>>>>>>>week num ")
                println("ndersforWeekWeekNum( "+ getRemindersforWeekWeekNum(ReminderUtil.WhenSelectType.WEEKNUM.name,weekDayName, i+1))
                println("getRemindersforMonthWeekWeekNum "+ getRemindersforMonthWeekWeekNum(ReminderUtil.WhenSelectType.MONTHWEEKNUM.name,month,weekDayName,i+1))*/
                todaysReminders.addAll(getRemindersforWeekWeekNum(ReminderUtil.WhenSelectType.WEEKNUM.name,weekDayName, i+1))
                todaysReminders.addAll(getRemindersforMonthWeekWeekNum(ReminderUtil.WhenSelectType.MONTHWEEKNUM.name,month,weekDayName,i+1))
            }
        }
        println("reminders size " + todaysReminders.size)

        emit(todaysReminders)
    }

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM reminder_master WHERE reminder_id = :reminderId")
    fun getReminderMasterMonth(reminderId: Long): ReminderMasterMonth

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM reminder_master WHERE reminder_id = :reminderId")
    fun getReminderMasterDate(reminderId: Long): ReminderMasterDate

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM reminder_master WHERE reminder_id = :reminderId")
    fun getReminderMasterWeek(reminderId: Long): ReminderMasterWeek

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM reminder_master WHERE reminder_id = :reminderId")
    fun getReminderMasterWeekNum(reminderId: Long): ReminderMasterWeeknum

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Ignore
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM reminder_master WHERE reminder_id = :reminderId")
    fun getReminderMaster(reminderId: Long): ReminderMaster

    @Ignore
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    suspend fun getReminder(reminderId: Long): Flow<HataReminder> = flow{
        println("getReminder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  "+reminderId)

        val reminderMaster = getReminderMaster(reminderId)
        println("getReminder >> >>>>>>>>>>>>>>>>>> "+reminderMaster.reminderOption)

        val months = mutableListOf<String>()
        getReminderMasterMonth(reminderId).reminderMonths.map {
            months.add(it.reminderMonth)
        }
        println("getReminder >>>>>>>>>>>>>>>"+months[0])

        val dates = mutableListOf<Int>()
        getReminderMasterDate(reminderId).reminderDates.map {
            dates.add(it.reminderDate)
        }

        val weeks = mutableListOf<String>()
        getReminderMasterWeek(reminderId).reminderWeeks.map{
            weeks.add(it.reminderWeek)
        }

        val weekNums = mutableListOf<Int>()
        getReminderMasterWeekNum(reminderId).reminderWeekNums.map {
            weekNums.add(it.reminderWeekNum)
        }

        emit ( HataReminder(
                reminderId = reminderId,
                reminderTime = reminderMaster.reminderTime,
                reminderMonths = months,
                reminderDates = dates,
                reminderWeeks = weeks,
                reminderWeekNum = weekNums,
                reminderOption = reminderMaster.reminderOption,
                remoptPickDate = reminderMaster.remoptPickDate,
                remCustomWhenSelectType = reminderMaster.remCustomWhenSelectType,
                alarmScreenVal = reminderMaster.alarmScreenVal,
                reminderEndYear = 0,
                reminderFormat = ""
        ) )
    }

}