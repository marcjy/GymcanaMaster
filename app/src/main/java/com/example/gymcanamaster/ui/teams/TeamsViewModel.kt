package com.example.gymcanamaster.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymcanamaster.data.repository.friend.FriendRepository
import com.example.gymcanamaster.data.repository.team.TeamRepository
import com.example.gymcanamaster.data.room.TeamWithFriends
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.data.room.team.TeamEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TeamsViewModel(
    private val teamRepository: TeamRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _teamToEdit = MutableStateFlow<TeamEntity?>(null)
    private val _teamToAddMembers = MutableStateFlow<TeamEntity?>(null)
    private val _displayAddFriendDialog = MutableStateFlow(false)
    private val _displayAddTeamDialog = MutableStateFlow(false)
    private val _displayEditTeamDialog = MutableStateFlow(false)


    private val _dialogState = combine(
        _teamToEdit,
        _teamToAddMembers,
        _displayAddFriendDialog,
        _displayAddTeamDialog,
        _displayEditTeamDialog
    ) {
        teamToEdit, teamToAddMembers, displayAddFriendDialog, displayAddTeamDialog, displayEditTeamDialog ->
        DialogState(
            teamToEdit = teamToEdit,
            teamToAddMembers = teamToAddMembers,
            displayAddFriendDialog = displayAddFriendDialog,
            displayAddTeamDialog = displayAddTeamDialog,
            displayEditTeamDialog = displayEditTeamDialog
        )
    }

    val teamUiState : StateFlow<TeamUiState> = combine(
        teamRepository.getAllTeamsWithFriends(),
        friendRepository.getAllFriends(),
        _dialogState
    ) { teamsWithFriends, friends, dialogState->
        TeamUiState(
            teamsWithFriends = teamsWithFriends,
            unassignedFriends = friends.filter { f -> f.teamId == null && f.isPlaying },
            dialogState = dialogState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TeamUiState()
    )


    fun setTeamToEdit(team : TeamEntity){ _teamToEdit.value = team }
    fun setTeamToAddMembers(team: TeamEntity){_teamToAddMembers.value = team}

    //Dialog
    fun enableAddFriendDialog() { _displayAddFriendDialog.value = true }
    fun disableAddFriendDialog() {
        _teamToAddMembers.value = null
        _displayAddFriendDialog.value = false
    }

    fun enableAddTeamDialog() {_displayAddTeamDialog.value = true}
    fun disableAddTeamDialog() { _displayAddTeamDialog.value = false }

    fun enableEditTeamDialog() {_displayEditTeamDialog.value = true}
    fun disableEditTeamDialog() { _displayEditTeamDialog.value = false }



    //Database actions
    fun addTeam(team: TeamEntity) {
        viewModelScope.launch {
            teamRepository.createTeam(team)
            disableAddTeamDialog()
        }
    }
    fun editTeam(team: TeamEntity) {
        viewModelScope.launch {
            teamRepository.updateTeam(team)
            disableEditTeamDialog()
        }
    }
    fun deleteTeam(team : TeamEntity){
        viewModelScope.launch {
            teamRepository.deleteTeam(team)
        }
    }

    fun addFriendsToTeam(teamId : Int, friendIds : List<Int>){
        viewModelScope.launch {
            friendRepository.addFriendsToTeam(teamId, friendIds)
            disableAddFriendDialog()
        }
    }

    fun removeFriendFromTeam(friend : FriendEntity){
        viewModelScope.launch {
            friendRepository.updateFriend(
                friend.copy(
                    teamId = null
                )
            )
        }
    }
}

data class TeamUiState(
    val teamsWithFriends: List<TeamWithFriends> = emptyList(),
    val unassignedFriends : List<FriendEntity> = emptyList(),
    val dialogState: DialogState = DialogState()
)

data class DialogState(
    val teamToAddMembers : TeamEntity? = null,
    val teamToEdit : TeamEntity? = null,
    val displayAddFriendDialog : Boolean = false,
    val displayAddTeamDialog : Boolean = false,
    val displayEditTeamDialog : Boolean = false
)