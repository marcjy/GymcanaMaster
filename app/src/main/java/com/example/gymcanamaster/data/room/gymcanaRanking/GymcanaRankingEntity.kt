package com.example.gymcanamaster.data.room.gymcanaRanking

import androidx.room.Entity

@Entity(
    tableName = "gymcana_ranking",
    primaryKeys = ["gameId", "friendId"]
)
data class GymcanaRankingEntity(
    val gameId: Int,
    val friendId: Int,
    val teamId: Int?,
    val awardedPoints: Int = 0
)
