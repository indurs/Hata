package com.oi.hata.task.data.model

import androidx.room.*
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import java.time.OffsetDateTime

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = ReminderMaster::class,
            parentColumns = ["reminder_id"],
            childColumns = ["task_reminder_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["group_id"],
            childColumns = ["task_group_id"],
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["group_id"],
            childColumns = ["important_group_id"],
        )
],
    indices = [Index(value = ["task_reminder_id"]),Index(value = ["task_group_id"]),Index(value = ["important_group_id"])]
)
data class Task(
    @ColumnInfo(name = "task_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "task_reminder_id") var taskReminderId: Long?,
    @ColumnInfo(name = "task_group_id") var taskGroupId: Long = 0,
    @ColumnInfo(name = "important_group_id") var importantGroupId: Long,
    @ColumnInfo(name = "task") var task: String,
    @ColumnInfo(name = "tag") var tag: String,
    @ColumnInfo(name = "task_due_date") var taskDueDate: Int,
    @ColumnInfo(name = "task_due_month") var taskDueMonth: Int,
    @ColumnInfo(name = "task_due_year") var taskDueYear: Int,
    @ColumnInfo(name = "completed") var completed: Boolean,
    @ColumnInfo(name = "today_task") var todaytask: Boolean,
    @ColumnInfo(name = "task_create_date") var taskCreateDate: OffsetDateTime?
    )
