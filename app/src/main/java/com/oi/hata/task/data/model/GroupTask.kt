package com.oi.hata.task.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class
GroupTask(
    @Embedded
    val Group: Group?,
    @Relation(
        parentColumn = "group_id",
        entityColumn = "task_group_id"
    )
    val tasks: List<Task>?
)
