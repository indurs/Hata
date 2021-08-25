package com.oi.hata.task.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class
ImportantGroupTask(
    @Embedded
    val Group: Group?,
    @Relation(
        parentColumn = "group_id",
        entityColumn = "important_group_id"
    )
    val tasks: List<Task>?
)
