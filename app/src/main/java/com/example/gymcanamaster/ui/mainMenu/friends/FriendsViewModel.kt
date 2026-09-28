package com.example.gymcanamaster.ui.mainMenu.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymcanamaster.data.repository.FriendRepository
import com.example.gymcanamaster.data.room.friend.FriendEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FriendsViewModel(private val friendRepository: FriendRepository) : ViewModel() {


    private val _isAddDialogEnabled = MutableStateFlow(false)
    private val _friendToEdit = MutableStateFlow<FriendEntity?>(null)

    val uiState: StateFlow<FriendUiState> = combine(
        friendRepository.getAllFriends(),
        _isAddDialogEnabled,
        _friendToEdit
    ) { friends, isAddVisible, friendToEdit ->
        FriendUiState(
            friendList = friends,
            isAddFriendDialogVisible = isAddVisible,
            friendToEdit = friendToEdit
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FriendUiState()
    )


    //Dialog
    fun enableAddFriendDialog() { _isAddDialogEnabled.value = true }
    fun disableAddFriendDialog() { _isAddDialogEnabled.value = false }

    fun setFriendToEdit(friendToEdit: FriendEntity) {_friendToEdit.value = friendToEdit}
    fun disableEditFriendDialog() {_friendToEdit.value = null}

    //Database actions
    fun addFriend(friend: FriendEntity) {
        viewModelScope.launch {
            friendRepository.createFriend(friend)
            disableAddFriendDialog()
        }
    }
    fun editFriend(friend: FriendEntity) {
        viewModelScope.launch {
            friendRepository.updateFriend(friend)
            disableEditFriendDialog()
        }
    }
    fun deleteFriend(friend: FriendEntity) {
        viewModelScope.launch {
            friendRepository.deleteFriend(friend)
        }
    }

}

data class FriendUiState(
    val friendList: List<FriendEntity> = emptyList(),
    val isAddFriendDialogVisible: Boolean = false,
    val friendToEdit: FriendEntity? = null
)