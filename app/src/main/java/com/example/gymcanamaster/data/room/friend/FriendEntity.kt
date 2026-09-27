package com.example.gymcanamaster.data.room.friend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    var isPlaying: Boolean = true
)