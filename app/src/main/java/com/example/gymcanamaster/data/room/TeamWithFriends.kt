package com.example.gymcanamaster.data.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.data.room.team.TeamEntity

data class TeamWithFriends(
    @Embedded val teamEntity: TeamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "teamId"
    )
    val friends: List<FriendEntity>
)
