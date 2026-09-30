package com.example.gymcanamaster.data.repository.gymcanaRanking

import com.example.gymcanamaster.data.room.gymcanaRanking.GymcanaRankingEntity
import kotlinx.coroutines.flow.Flow

interface GymcanaRankingRepository {

    suspend fun insertOrUpdateRanking(gymcanaRanking: GymcanaRankingEntity)
    suspend fun insertOrUpdateRankings(gymcanaRanking: List<GymcanaRankingEntity>)
    fun getGymcanaRankingForGame(gameId: Int): Flow<List<GymcanaRankingEntity>>
    suspend fun resetTable()
}