package com.example.gymcanamaster.ui.finishScreen

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymcanamaster.R
import com.example.gymcanamaster.data.AppViewModelProvider
import com.example.gymcanamaster.ui.TransparentTopAppVBar
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme

@Composable
fun FinishScreen(
    modifier: Modifier = Modifier,
    finishScreenViewModel: FinishScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),

    onNavigateToExit : () -> Unit
){
    val finishScreenUiState by finishScreenViewModel.finishScreenUiState.collectAsStateWithLifecycle()

    FinishScreenContent(
        finishScreenUiState = finishScreenUiState,
        onClickExit = {finishScreenViewModel.onExitGame(onNavigateToExit)},
        modifier = modifier
    )
}

@Composable
fun FinishScreenContent(
    finishScreenUiState: FinishScreenUiState,

    onClickExit: () -> Unit,

    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TransparentTopAppVBar(R.string.f_title_finish_screen) },
        containerColor = Color.Transparent,
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                    .weight(1.0f)
                    .graphicsLayer {alpha = 0.75f}
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(dimensionResource(R.dimen.small_padding)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.tiny_padding)),
                ) {
                    itemsIndexed(
                        items = finishScreenUiState.friendScoreList
                    ) { index, friendScore ->

                        FriendScoreCard(
                            medalIconResId = getMedalIconRes(index),
                            friendName = friendScore.friendName,
                            friendScore = friendScore.totalScore,
                            indexForColoring = index
                        )
                    }
                }
            }

                //Exit button
                Button(
                    onClick = { onClickExit() },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.small_padding))
                ) {
                    Text(
                        text = stringResource(R.string.g_exit_game_button_text),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                    )
                }
        }
    }
}

@Composable
private fun FriendScoreCard(
    @DrawableRes medalIconResId: Int?,
    friendName : String,
    friendScore: Int,
    indexForColoring: Int,
    modifier: Modifier = Modifier
){

    val cardBackgroundColor = when (indexForColoring) {
        0 -> Color(0xFFFFD700).copy(alpha = 0.25f) // Gold tint
        1 -> Color(0xFFC0C0C0).copy(alpha = 0.25f) // Silver tint
        2 -> Color(0xFFCD7F32).copy(alpha = 0.25f) // Bronze tint
        else -> if (indexForColoring % 2 == 0)
            Color(0xFF381E72).copy(alpha = 0.60f)
        else Color(0xFF25134A).copy(alpha = 0.60f)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.2f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.tiny_padding))
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.huge_padding),
                        top = dimensionResource(R.dimen.small_padding),
                        bottom = dimensionResource(R.dimen.small_padding))
            ) {
                if (medalIconResId != null) {
                    Image(
                        painter = painterResource(medalIconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                } else
                    Spacer(modifier = Modifier.size(40.dp))

                Text(
                    text = friendName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                )
            }

            Spacer(Modifier.weight(1.0f))

            Text(
                text = "$friendScore pts",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.huge_padding))
            )
        }
    }
}

@DrawableRes
private fun getMedalIconRes(position: Int): Int? = when (position) {
    0 -> R.drawable.gold_medal
    1 -> R.drawable.silver_medal
    2 -> R.drawable.bronze_medal
    else -> null
}

//region Previews
@Composable
@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:width=411dp,height=891dp"
)
private fun FinishScreenPreviewDark() {
    GymcanaMasterTheme {
        FinishScreenContent(
            finishScreenUiState = FinishScreenUiState(),
            onClickExit = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL, device = "spec:width=411dp,height=891dp"
)
private fun FinishScreenPreview() {
    GymcanaMasterTheme {
        FinishScreenContent(
            finishScreenUiState = FinishScreenUiState(),
            onClickExit = {}
        )
    }
}
//endregion