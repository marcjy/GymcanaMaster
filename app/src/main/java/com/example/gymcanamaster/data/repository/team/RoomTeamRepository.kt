package com.example.gymcanamaster.data.repository.team

import com.example.gymcanamaster.data.room.relations.TeamWithFriends
import com.example.gymcanamaster.data.room.team.TeamDao
import com.example.gymcanamaster.data.room.team.TeamEntity
import kotlinx.coroutines.flow.Flow

class RoomTeamRepository(private val teamDao: TeamDao) : TeamRepository {
    override suspend fun createTeam(team: TeamEntity) = teamDao.insert(team)

    override suspend fun deleteTeam(team: TeamEntity) = teamDao.delete(team)

    override suspend fun updateTeam(team: TeamEntity) = teamDao.update(team)

    override fun getAllTeams(): Flow<List<TeamEntity>> = teamDao.getAll()
    override fun getAllTeamsWithFriends(): Flow<List<TeamWithFriends>> = teamDao.getAllTeamsWithFriends()
}