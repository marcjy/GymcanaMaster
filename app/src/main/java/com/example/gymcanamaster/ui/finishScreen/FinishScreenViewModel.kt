package com.example.gymcanamaster.ui.finishScreen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymcanamaster.data.repository.gymcanaRanking.GymcanaRankingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinishScreenViewModel(
    private val gymcanaRankingRepository: GymcanaRankingRepository
) : ViewModel() {

    val finishScreenUiState: StateFlow<FinishScreenUiState> =
        gymcanaRankingRepository.getFriendsWithTotalScore()
            .map {
                FinishScreenUiState(
                    friendScoreList = it,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = FinishScreenUiState()
            )

    fun onExitGame(
        onCompleted : () -> Unit
    ) {
        viewModelScope.launch {
            gymcanaRankingRepository.resetTable()
            onCompleted()
        }
    }
}

@Immutable
data class FinishScreenUiState(
    val friendScoreList : List<FriendScoreDetails> = emptyList()
)

@Immutable
data class FriendScoreDetails(
    val friendId: Int,
    val friendName : String,
    val totalScore : Int
)