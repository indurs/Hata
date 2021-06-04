package com.oi.hata.common.reminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oi.hata.common.reminder.data.local.dao.ReminderDao
import com.oi.hata.common.reminder.data.local.model.*

@TypeConverters(Converters::class)
@Database(entities = [ReminderMaster::class,ReminderMonth::class,ReminderDate::class,ReminderWeek::class,ReminderWeekNum::class], version = 1, exportSchema = false)
abstract class HataDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: HataDatabase? = null

        fun getInstance(context: Context): HataDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): HataDatabase {
            return Room.databaseBuilder(context, HataDatabase::class.java, DATABASE_NAME).build()
        }
    }
}

private const val DATABASE_NAME = "Hata"