package com.example.gymcanamaster.data.room.gymcanaRanking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymcanamaster.ui.finishScreen.FriendScoreDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface GymcanaRankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRanking(gymcanaRanking: GymcanaRankingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRankings(gymcanaRanking: List<GymcanaRankingEntity>)


    @Query("SELECT * FROM gymcana_ranking WHERE gameId = :gameId")
    fun getGymcanaRankingForGame(gameId: Int): Flow<List<GymcanaRankingEntity>>

    @Query(
        """
            SELECT
                f.id as friendId,
                f.name AS friendName,
                SUM(r.awardedPoints) AS totalScore
            FROM
                gymcana_ranking AS r
            INNER JOIN
                friends AS f ON
                    f.id = r.friendId
            GROUP BY
                f.id
            ORDER BY
                totalScore DESC
        """
    )
    fun getFriendWithTotalScore() : Flow<List<FriendScoreDetails>>

    @Query("DELETE FROM gymcana_ranking")
    suspend fun resetTable()
}