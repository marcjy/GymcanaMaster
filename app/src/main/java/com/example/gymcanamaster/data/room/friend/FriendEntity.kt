package com.example.gymcanamaster.data.room.friend

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.gymcanamaster.data.room.team.TeamEntity

@Entity(
    tableName = "friends",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["teamId"])
    ]
)
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isPlaying: Boolean = true,
    val teamId: Int? = null
)