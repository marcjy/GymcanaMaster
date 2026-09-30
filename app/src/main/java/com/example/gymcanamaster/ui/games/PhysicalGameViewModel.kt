package com.example.gymcanamaster.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymcanamaster.data.repository.friend.FriendRepository
import com.example.gymcanamaster.data.repository.gymcanaRanking.GymcanaRankingRepository
import com.example.gymcanamaster.data.repository.team.TeamRepository
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.data.room.gymcanaRanking.GymcanaRankingEntity
import com.example.gymcanamaster.data.room.team.TeamEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhysicalGameViewModel (
    private val friendRepository: FriendRepository,
    private val gymcanaRankingRepository: GymcanaRankingRepository,
    private val teamRepository: TeamRepository
    ) : ViewModel() {

    private val _selectedRanks = MutableStateFlow<Map<Int, Badges?>>(emptyMap())

    fun initGame(
        gameId : Int,
        isTeamBasedGame : Boolean
    ){
        viewModelScope.launch {
            val savedRanking = gymcanaRankingRepository.getGymcanaRankingForGame(gameId).first()

            if(savedRanking.isNotEmpty()){
                val mapWithPoints = mutableMapOf<Int, Badges?>()

                if (isTeamBasedGame){
                    savedRanking
                        .filter { it.teamId != null }
                        .distinctBy { it.teamId }
                        .forEach { team ->
                            mapWithPoints[team.teamId!!] = team.awardedPoints.toBadgeOrNull()
                        }
                } else{
                    savedRanking.forEach { friend ->
                        mapWithPoints[friend.friendId] = friend.awardedPoints.toBadgeOrNull()
                    }
                }

                _selectedRanks.value = mapWithPoints
            }
            else
                _selectedRanks.value = emptyMap()
        }
    }

    val physicalGameUiState: StateFlow<PhysicalGameUiState> = combine(
        friendRepository.getAllFriends(),
        teamRepository.getAllTeams(),
        _selectedRanks
    ) { friends, teams, selectedRanks ->
        PhysicalGameUiState(
            friendsList = friends,
            teamsList = teams,
            rankableAndBadge = selectedRanks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PhysicalGameUiState()
    )


    fun onSelectedRank(rankableId: Int, rank: Badges?) {
        _selectedRanks.update { currentRanks ->
            currentRanks.toMutableMap().apply {

                if (rank != null) {
                    val rankableHoldingRank = entries.firstOrNull { it.value == rank }?.key
                    if (rankableHoldingRank != null && rankableHoldingRank != rankableId) {
                        this[rankableHoldingRank] = null
                    }
                }
                this[rankableId] = rank
            }
        }
    }

    //Database action
    fun awardPoints(
        gameId : Int,
        isTeamBasedGame: Boolean,
        onCompleted: () -> Unit
    ) {
        viewModelScope.launch {
            val teamsAndFriends = teamRepository.getAllTeamsWithFriends().first()
            val friends = friendRepository.getAllFriends().first()
            val rankingToUpdate = mutableListOf<GymcanaRankingEntity>()

            _selectedRanks.value.forEach { (rankableId, badge) ->
                    if (isTeamBasedGame) {
                        val teamAndFriends = teamsAndFriends.firstOrNull{ it.teamEntity.id == rankableId }
                        teamAndFriends?.friends?.forEach { friend->
                            rankingToUpdate.add(
                                GymcanaRankingEntity(
                                    gameId = gameId,
                                    friendId = friend.id,
                                    teamId = teamAndFriends.teamEntity.id,
                                    awardedPoints = badge?.points ?: 0
                                )
                            )
                        }
                    } else {
                        friends.filter { it.id == rankableId }.forEach { friend ->
                            rankingToUpdate.add(
                                GymcanaRankingEntity(
                                    gameId = gameId,
                                    friendId = friend.id,
                                    teamId = null,
                                    awardedPoints = badge?.points ?: 0
                                )
                            )
                        }
                    }
            }

            if (rankingToUpdate.isNotEmpty())
                gymcanaRankingRepository.insertOrUpdateRankings(rankingToUpdate)

            onCompleted()
        }
    }
}

data class PhysicalGameUiState(
    val friendsList : List<FriendEntity> = emptyList(),
    val teamsList : List<TeamEntity> = emptyList(),
    val rankableAndBadge : Map<Int, Badges?> = emptyMap()
){
    fun allBadgesAssigned() : Boolean{
        return rankableAndBadge.values.filterNotNull().containsAll(Badges.entries)
    }
}

enum class Badges(val points : Int)
{
    FIRST_PLACE(7),
    SECOND_PLACE(5),
    THIRD_PLACE(3)
}

fun Int.toBadgeOrNull() : Badges?{
    return when(this){
        Badges.FIRST_PLACE.points -> Badges.FIRST_PLACE
        Badges.SECOND_PLACE.points -> Badges.SECOND_PLACE
        Badges.THIRD_PLACE.points -> Badges.THIRD_PLACE
        else -> null
    }
}
