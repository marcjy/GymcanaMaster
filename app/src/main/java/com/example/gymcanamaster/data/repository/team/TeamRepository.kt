package com.example.gymcanamaster.data.repository.team

import com.example.gymcanamaster.data.room.TeamWithFriends
import com.example.gymcanamaster.data.room.team.TeamEntity
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun createTeam(team : TeamEntity)
    suspend fun deleteTeam(team: TeamEntity)
    suspend fun updateTeam(team: TeamEntity)

    fun getAllTeams(): Flow<List<TeamEntity>>
    fun getAllTeamsWithFriends(): Flow<List<TeamWithFriends>>
}