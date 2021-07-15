package com.oi.hata.task.data.dao

import androidx.room.*
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

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM task WHERE task_id = :taskId ")
    fun getTask(taskId: Long): Task

}