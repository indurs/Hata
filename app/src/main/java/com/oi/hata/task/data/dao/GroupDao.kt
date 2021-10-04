package com.oi.hata.task.data.dao

import androidx.room.*
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.ImportantGroupTask
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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

    @Transaction
    @Query("SELECT * FROM taskgroup WHERE group_id = :groupId ")
    fun getTaskGroup(groupId: Long): Flow<GroupTask>

    @Transaction
    @Query("SELECT * FROM task where task.important_group_id = 2 and task_group_id != 2")
    suspend fun getImportantTasks(): List<Task>

    @Transaction
    @Query("SELECT * FROM taskgroup WHERE name = :groupName ")
    suspend fun getTaskGroup(groupName: String): GroupTask

    @Transaction
    @Query("SELECT * FROM taskgroup ")
    fun getTaskGroups(): Flow<List<GroupTask>>

    @Transaction
    suspend fun getTasksForGroup(groupName: String) = flow {

        var groupTask: GroupTask
        var tasks = mutableListOf<Task>()

        if(groupName == "Important"){
            tasks.clear()
            coroutineScope {
                launch(Dispatchers.IO){
                    var importantTasks = getImportantTasks()

                    importantTasks.let {
                        if (it != null) {
                            tasks.addAll(it)
                        }
                    }
                }

                getTaskGroup(groupName).tasks.let {
                    if (it != null) {
                        tasks.addAll(it)
                    }
                }

                groupTask = GroupTask(Group(2,"Important"),tasks.sortedByDescending { task -> task.taskCreateDate })

            }


        }else{
            var grpTask = getTaskGroup(groupName)
            grpTask.tasks.let {
                if (it != null) {
                    tasks.addAll(it)
                }
            }
            groupTask = GroupTask(Group(grpTask.Group!!.id,groupName),tasks.sortedByDescending { task -> task.taskCreateDate })
        }

        emit(groupTask)
    }


}