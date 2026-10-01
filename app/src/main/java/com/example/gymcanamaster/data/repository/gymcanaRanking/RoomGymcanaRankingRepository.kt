package com.example.gymcanamaster.data.repository.gymcanaRanking

import com.example.gymcanamaster.data.room.gymcanaRanking.GymcanaRankingDao
import com.example.gymcanamaster.data.room.gymcanaRanking.GymcanaRankingEntity
import com.example.gymcanamaster.ui.finishScreen.FriendScoreDetails
import kotlinx.coroutines.flow.Flow

class RoomGymcanaRankingRepository(private val gymcanaRankingDao: GymcanaRankingDao) : GymcanaRankingRepository {

    override suspend fun insertOrUpdateRanking(gymcanaRanking: GymcanaRankingEntity) = gymcanaRankingDao.insertOrUpdateRanking(gymcanaRanking)
    override suspend fun insertOrUpdateRankings(gymcanaRanking: List<GymcanaRankingEntity>) = gymcanaRankingDao.insertOrUpdateRankings(gymcanaRanking)

    override fun getGymcanaRankingForGame(gameId: Int): Flow<List<GymcanaRankingEntity>> = gymcanaRankingDao.getGymcanaRankingForGame(gameId)
    override fun getFriendsWithTotalScore(): Flow<List<FriendScoreDetails>> = gymcanaRankingDao.getFriendWithTotalScore()

    override suspend fun resetTable() = gymcanaRankingDao.resetTable()
}