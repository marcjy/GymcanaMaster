package com.example.gymcanamaster.ui.games

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymcanamaster.R
import com.example.gymcanamaster.data.AppViewModelProvider
import com.example.gymcanamaster.ui.GameBottomNavigationBar
import com.example.gymcanamaster.ui.TransparentTopAppVBar
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme


@Composable
fun PhysicalGameScreen(
    modifier: Modifier = Modifier,
    @StringRes gameTitleResId: Int,

    currentGameIndex: Int,
    maxGameIndex: Int,
    isTeamBasedGame : Boolean,
    onNavigateToPreviousGame: () -> Unit,
    onNavigateToNextGame: () -> Unit,

    physicalGameViewModel: PhysicalGameViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val physicalGameUiState by physicalGameViewModel.physicalGameUiState.collectAsStateWithLifecycle()

    LaunchedEffect(currentGameIndex, isTeamBasedGame) {
        physicalGameViewModel.initGame(
            gameId = currentGameIndex,
            isTeamBasedGame = isTeamBasedGame
        )
    }

    PhysicalGameScreenContent(
        modifier = modifier,

        gameTitleResId = gameTitleResId,
        physicalGameUiState = physicalGameUiState,

        onSelectedRank = physicalGameViewModel::onSelectedRank,
        
        currentGameIndex = currentGameIndex,
        maxGameIndex = maxGameIndex,
        isTeamBasedGame = isTeamBasedGame,
        onNavigateToPreviousGame = onNavigateToPreviousGame,
        onNavigateToNextGame = {physicalGameViewModel.awardPoints(
            gameId = currentGameIndex,
            isTeamBasedGame = isTeamBasedGame,
            onCompleted = onNavigateToNextGame
        )},
    )
}

@Composable
private fun PhysicalGameScreenContent(
    @StringRes gameTitleResId: Int,
    physicalGameUiState: PhysicalGameUiState,

    onSelectedRank: (Int, Badges?) -> Unit,

    currentGameIndex: Int,
    maxGameIndex: Int,
    isTeamBasedGame : Boolean,
    onNavigateToPreviousGame: () -> Unit,
    onNavigateToNextGame: () -> Unit,

    modifier: Modifier = Modifier
){

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = { TransparentTopAppVBar(gameTitleResId) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_padding))
                    .clip(RoundedCornerShape(16.dp))
                    .weight(1.0f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(dimensionResource(R.dimen.medium_padding)),
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    if (isTeamBasedGame) {
                        items(
                            items = physicalGameUiState.teamsList,
                            key = { it.id }
                        ) { team ->
                            RankableAndBadgesCard(
                                selectedRank = physicalGameUiState.rankableAndBadge[team.id],
                                onSelectedRank = onSelectedRank,
                                rankableId = team.id,
                                rankableName = team.name
                            )
                        }
                    } else {
                        items(
                            items = physicalGameUiState.friendsList,
                            key = { it.id }
                            ) { friend ->
                            RankableAndBadgesCard(
                                selectedRank = physicalGameUiState.rankableAndBadge[friend.id],
                                onSelectedRank = onSelectedRank,
                                rankableId = friend.id,
                                rankableName = friend.name
                            )
                        }
                    }
                }
            }

            GameBottomNavigationBar(
                currentGameIndex = currentGameIndex,
                maxGameIndex = maxGameIndex,
                isNextButtonEnabled = physicalGameUiState.allBadgesAssigned(),
                onNavigateToPreviousGame = onNavigateToPreviousGame,
                onNavigateToNextGame = onNavigateToNextGame
            )
        }
    }
}

@Composable
private fun RankableAndBadgesCard(
    selectedRank: Badges?,
    onSelectedRank: (Int, Badges?) -> Unit,
    rankableId : Int,
    rankableName : String,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.small_padding))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {
            Text(
                text = rankableName,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            SegmentedBadgeButton(
                rankableId = rankableId,
                selectedRank = selectedRank,
                onSelectedRank = onSelectedRank
            )
        }
    }
}

@Composable
private fun SegmentedBadgeButton(
    rankableId : Int,
    selectedRank : Badges?,
    onSelectedRank : (Int, Badges?) -> Unit,
    modifier: Modifier = Modifier
) {
    val ranks = Badges.entries

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),
    ) {
        ranks.forEachIndexed { index, rank ->

            val isSelected = rank == selectedRank

            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index, ranks.size),
                selected = isSelected,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if(isSelected)
                        onSelectedRank(rankableId, null) //If the button is already selected, unselect it
                    else
                        onSelectedRank(rankableId, rank)
                },
                icon = {}, //No icon for this button
                label = {
                    Text(
                        text = when(rank){
                            Badges.FIRST_PLACE -> stringResource(R.string.g_first_place_badge)
                            Badges.SECOND_PLACE -> stringResource(R.string.g_second_place_badge)
                            Badges.THIRD_PLACE -> stringResource(R.string.g_third_place_badge)
                        }
                    )
                }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
private fun PhysicalGameScreenContentPreview(){
    GymcanaMasterTheme {
        PhysicalGameScreenContent(
            R.string.g_game_title_chopstick_transfer,
            physicalGameUiState = PhysicalGameUiState(),
            onSelectedRank = { _, _ -> },
            currentGameIndex = 1,
            maxGameIndex = 3,
            onNavigateToPreviousGame = {},
            onNavigateToNextGame = {},
            isTeamBasedGame = true
        )
    }
}