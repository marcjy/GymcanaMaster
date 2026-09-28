package com.example.gymcanamaster.data.room.team

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val name : String,
)