package com.oi.hata.common.reminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.data.local.model.*
import com.oi.hata.task.data.dao.GroupDao
import com.oi.hata.task.data.dao.TaskDao
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.*

@TypeConverters(Converters::class)
@Database(
    entities = [ReminderMaster::class, ReminderMonth::class, ReminderDate::class, ReminderWeek::class, ReminderWeekNum::class, Task::class, Group::class],
    version = 1,
    exportSchema = false
)
abstract class HataDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun taskDao(): TaskDao
    abstract fun groupDao(): GroupDao

    companion object {

        @Volatile
        private var instance: HataDatabase? = null

        fun getInstance(context: Context): HataDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): HataDatabase {
            return Room.databaseBuilder(context, HataDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        CoroutineScope(Dispatchers.IO).launch {
                            getInstance(context).groupDao().insertGroups(groups)
                        }
                    }
                })
                .build()
        }
    }
}

private const val DATABASE_NAME = "Hata"
private val groups = listOf<Group>(
    Group(1, "Tasks"), Group(2, "Important"), Group(3, "Assignment"),
    Group(4, "Meeting"), Group(5, "General"), Group(6, "App Design"), Group(7, "Gardening"),
    Group(8, "Groceries"), Group(9, "Cleaning"), Group(10, "Exercise"),
    Group(11, "Reading"), Group(12, "New Skills"), Group(13, "Cooking"),
    Group(14, "Payments"), Group(15, "Birthday Party"), Group(16, "Project"),
    Group(17, "Appointments"), Group(18, "Picnic"), Group(19, "Laundry"),
    Group(20, "Travel"), Group(21, "Bookings"), Group(22, "Budget"),
)