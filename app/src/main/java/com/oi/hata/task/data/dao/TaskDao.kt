package com.oi.hata.task.data.dao

import androidx.room.*
import com.oi.hata.task.data.model.CalendarTaskItem
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task):Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Transaction
    @Query("SELECT * FROM task WHERE task_id = :taskId ")
    suspend fun getTask(taskId: Long): Task

    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,task_due_month,task_due_year,important_group_id,completed,today_task,task_create_date FROM task 
                        WHERE task_reminder_id IS NULL  AND 
                     task_due_month = :month and task_due_date = :date AND task_due_year = :year ORDER BY datetime(task_create_date) DESC
                         """)
    suspend fun getDueTasksForDay(month: Int,date:Int,year:Int): List<Task>

    @Transaction
    @Query(""" SELECT task_id,task,task_due_date,important_group_id,completed,task_create_date FROM task 
                     WHERE task_reminder_id IS NULL and task_due_month = :month ORDER BY datetime(task_create_date) DESC
                         """)
    suspend fun getDueTasks(month: Int): List<CalendarTaskItem>

    @Transaction
    @Query(""" SELECT task_id,task_reminder_id,task_group_id,task,tag,task_due_date,task_due_month,task_due_year,important_group_id,completed,today_task,task_create_date FROM 
                        task WHERE today_task = :todayTask ORDER BY datetime(task_create_date) DESC """)
    suspend fun getTodayTasks(todayTask: Boolean): List<Task>

    @Transaction
    @Query("SELECT Count(*) FROM task where task.important_group_id = 2 and task_group_id != 2")
    fun getImportantTasksCount(): Flow<Int>


}