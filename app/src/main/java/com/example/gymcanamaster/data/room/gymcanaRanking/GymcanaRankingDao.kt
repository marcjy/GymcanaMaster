package com.example.gymcanamaster.data.room.gymcanaRanking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GymcanaRankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRanking(gymcanaRanking: GymcanaRankingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRankings(gymcanaRanking: List<GymcanaRankingEntity>)


    @Query("SELECT * FROM gymcana_ranking WHERE gameId = :gameId")
    fun getGymcanaRankingForGame(gameId: Int): Flow<List<GymcanaRankingEntity>>

    @Query("DELETE FROM gymcana_ranking")
    suspend fun resetTable()
}