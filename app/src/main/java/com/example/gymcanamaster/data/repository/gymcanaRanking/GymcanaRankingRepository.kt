package com.example.gymcanamaster.data.repository.gymcanaRanking

import com.example.gymcanamaster.data.room.gymcanaRanking.GymcanaRankingEntity
import com.example.gymcanamaster.ui.finishScreen.FriendScoreDetails
import kotlinx.coroutines.flow.Flow

interface GymcanaRankingRepository {

    suspend fun insertOrUpdateRanking(gymcanaRanking: GymcanaRankingEntity)
    suspend fun insertOrUpdateRankings(gymcanaRanking: List<GymcanaRankingEntity>)
    fun getGymcanaRankingForGame(gameId: Int): Flow<List<GymcanaRankingEntity>>
    fun getFriendsWithTotalScore(): Flow<List<FriendScoreDetails>>
    suspend fun resetTable()
}