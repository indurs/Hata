package com.oi.hata.task.data.dao

import androidx.room.*
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group):Long

    @Insert
    suspend fun insertGroups(groups: List<Group>)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM taskgroup WHERE group_id = :groupId ")
    fun getTaskGroup(groupId: Long): Flow<GroupTask>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM taskgroup WHERE name = :groupName ")
    fun getTaskGroup(groupName: String): Flow<GroupTask>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM taskgroup")
    fun getTaskGroups(): Flow<List<GroupTask>>

}