package com.example.gymcanamaster.ui.mainMenu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymcanamaster.R
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme


@Composable
fun MainMenu(
    onNavigateToFriendsAndTeams : () -> Unit,
    onNavigateToStart: () -> Unit,
    modifier : Modifier = Modifier
){
    MainMenuContent(
        onNavigateToFriendsAndTeams = onNavigateToFriendsAndTeams,
        onNavigateToStart = onNavigateToStart,
        modifier = modifier
    )
}

@Composable
fun MainMenuContent(
    onNavigateToFriendsAndTeams : () -> Unit,
    onNavigateToStart: () -> Unit,
    modifier : Modifier = Modifier
){
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = { GymcanaTopAppBar() },
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding))
        ) {

            Spacer(Modifier.height(72.dp))
            FriendsButton(
                onNavigateToFriends = onNavigateToFriendsAndTeams
            )
            Spacer(Modifier.height(48.dp))
            MenuButtons(
                onNavigateToStart = onNavigateToStart
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GymcanaTopAppBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(
        title = {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
private fun FriendsButton(
    onNavigateToFriends : () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onNavigateToFriends() },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(92.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.team_icon),
                contentDescription = null,
                modifier = Modifier.size(54.dp)
            )

            Text(
                text = stringResource(R.string.mn_friends_button),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun MenuButtons(
    onNavigateToStart : () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.medium_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding))
    ) {
        StartButton(
            onNavigateToTeams = onNavigateToStart
        )
        GamesButton()
    }
}

@Composable
private fun StartButton(
    onNavigateToTeams : () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = {onNavigateToTeams() },
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth(),

    ){
        Text(
            text = stringResource(R.string.mn_start_button),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
@Composable
private fun GamesButton(modifier: Modifier = Modifier){
    Button(
        onClick = { /*TODO*/ },
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth(),

        ){
        Text(
            text = stringResource(R.string.mn_games_button),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES )
@Composable
fun MainMenuPreviewDark() {
    GymcanaMasterTheme() {
        MainMenuContent(
            onNavigateToFriendsAndTeams = { },
            onNavigateToStart = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO )
@Composable
fun MainMenuPreview() {
    GymcanaMasterTheme() {
        MainMenuContent(
            onNavigateToFriendsAndTeams = { },
            onNavigateToStart = {}
        )
    }
}