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
@Database(entities = [ReminderMaster::class,ReminderMonth::class,ReminderDate::class,ReminderWeek::class,ReminderWeekNum::class, Task::class, Group::class], version = 1, exportSchema = false)
abstract class HataDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun taskDao(): TaskDao
    abstract fun groupDao(): GroupDao

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
            return Room.databaseBuilder(context, HataDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // insert the data on the IO Thread
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
private val groups = listOf<Group>(Group(1,"Tasks"),Group(2,"Assignment"), Group(3, "Groceries"),
    Group(4,"Meeting"), Group(5,"Project"), Group(6,"Picnic"), Group(7,"Event2"),
    Group(8,"Event3"), Group(9,"Event4"),Group(10,"Event5"),
    Group(11,"Event6"), Group(12,"Event7"),Group(13,"Event8"),
    Group(14,"Event9"), Group(15,"Event10"),Group(16,"Event11"),
    Group(17,"Event17"), Group(18,"Event18"),Group(19,"Event19"),
    Group(20,"Event20"), Group(21,"Event21"),Group(22,"Event22"),
)