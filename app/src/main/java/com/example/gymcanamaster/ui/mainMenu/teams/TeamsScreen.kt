package com.example.gymcanamaster.ui.mainMenu.teams

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymcanamaster.R
import com.example.gymcanamaster.data.AppViewModelProvider
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.data.room.team.TeamEntity
import com.example.gymcanamaster.ui.AddFloatingActionButton
import com.example.gymcanamaster.ui.AddPersonIconButton
import com.example.gymcanamaster.ui.EditIconButton
import com.example.gymcanamaster.ui.InputTextDialog
import com.example.gymcanamaster.ui.RemovePersonIconButton
import com.example.gymcanamaster.ui.TransparentTopAppVBar
import com.example.gymcanamaster.ui.TrashIconButton
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme

@Composable
fun TeamsScreen(
    modifier: Modifier = Modifier,
    teamsViewModel: TeamsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val teamUiState by teamsViewModel.teamUiState.collectAsStateWithLifecycle()
    TeamsScreenContent(
        teamUiState = teamUiState,
        modifier = modifier,

        onAddTeam = teamsViewModel::addTeam,
        onEditTeam = teamsViewModel::editTeam,
        onDeleteTeam = teamsViewModel::deleteTeam,

        onAddFriendsToTeam = teamsViewModel::addFriendsToTeam,
        onShowAddFriendsDialog = teamsViewModel::enableAddFriendDialog,
        onHideAddFriendsDialog = teamsViewModel::disableAddFriendDialog,
        onRemoveFriendFromTeam = teamsViewModel::removeFriendFromTeam,

        onShowAddTeamDialog = teamsViewModel::enableAddTeamDialog,
        onHideAddTeamDialog = teamsViewModel::disableAddTeamDialog,

        onShowEditTeamDialog = teamsViewModel::enableEditTeamDialog,
        onHideEditTeamDialog = teamsViewModel::disableEditTeamDialog,

        setTeamToEdit = teamsViewModel::setTeamToEdit,
        setTeamToAddMembers = teamsViewModel::setTeamToAddMembers
    )
}

@Composable
private fun TeamsScreenContent(
    modifier: Modifier = Modifier,
    teamUiState: TeamUiState,

    onAddTeam : (TeamEntity) -> Unit,
    onEditTeam : (TeamEntity) -> Unit,
    onDeleteTeam: (TeamEntity) -> Unit,

    onAddFriendsToTeam : (Int, List<Int>) -> Unit,
    onShowAddFriendsDialog : () -> Unit,
    onHideAddFriendsDialog : () -> Unit,
    onRemoveFriendFromTeam: (FriendEntity) -> Unit,

    onShowAddTeamDialog : () -> Unit,
    onHideAddTeamDialog: () -> Unit,

    onShowEditTeamDialog : () -> Unit,
    onHideEditTeamDialog: () -> Unit,

    setTeamToEdit : (TeamEntity) -> Unit,
    setTeamToAddMembers : (TeamEntity) -> Unit
) {
    Scaffold(
        topBar = { TransparentTopAppVBar(R.string.t_title_teams_screen) },
        containerColor = Color.Transparent,
        floatingActionButton = { AddFloatingActionButton(onShowAddTeamDialog) },
        modifier = modifier
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.fab_content_bottom_padding)),
            ) {
                items(
                    items = teamUiState.teamsWithFriends,
                    key = { it.teamEntity.id }
                ) {
                    TeamCard(
                        teamEntity = it.teamEntity,
                        teamMembers = it.friends,
                        onDeleteTeam = onDeleteTeam,
                        onShowAddFriendsDialog = onShowAddFriendsDialog,
                        onRemoveFriendFromTeam = onRemoveFriendFromTeam,
                        setTeamToEdit = setTeamToEdit,
                        setTeamToAddMembers = setTeamToAddMembers,
                        onShowEditTeamDialog = onShowEditTeamDialog
                    )
                }
            }
        }
    }

    if (teamUiState.dialogState.displayAddFriendDialog) {
        teamUiState.dialogState.teamToAddMembers?.let { team ->
            AddTeamMembersDialog(
                teamEntity = team,
                friendListWithoutTeam = teamUiState.unassignedFriends,
                onConfirm = { teamId, friendIds -> onAddFriendsToTeam(teamId, friendIds) },
                onCancel = { onHideAddFriendsDialog() }
            )
        }
    }

    if (teamUiState.dialogState.displayAddTeamDialog) {
        InputTextDialog(
            initialText = "",
            textFieldLabelResId = R.string.t_label_add_team_text_field,
            onConfirm = { newTeamName ->
                onAddTeam(TeamEntity(name = newTeamName))
            },
            onDismiss = { onHideAddTeamDialog() }
        )
    }

    if (teamUiState.dialogState.displayEditTeamDialog) {
        teamUiState.dialogState.teamToEdit?.let { team ->
            InputTextDialog(
                initialText = team.name,
                textFieldLabelResId = R.string.t_label_add_team_text_field,
                onConfirm = { newTeamName ->
                    onEditTeam(team.copy(name = newTeamName))
                },
                onDismiss = { onHideEditTeamDialog() }
            )
        }
    }
}


@Composable
private fun TeamCard(
    modifier: Modifier = Modifier,
    teamEntity: TeamEntity,
    teamMembers: List<FriendEntity> = emptyList(),

    onDeleteTeam: (TeamEntity) -> Unit,

    onShowAddFriendsDialog: () -> Unit,
    onRemoveFriendFromTeam: (FriendEntity) -> Unit,

    onShowEditTeamDialog: () -> Unit,

    setTeamToEdit : (TeamEntity) -> Unit,
    setTeamToAddMembers : (TeamEntity) -> Unit
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.big_padding),
                vertical = dimensionResource(R.dimen.small_padding)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TeamDetails(
                teamEntity = teamEntity,
                setTeamToEdit = setTeamToEdit,
                onDeleteTeam = onDeleteTeam,
                onShowEditTeamDialog = onShowEditTeamDialog
            )

            TeamMembers(
                onRemoveFriendFromTeam = onRemoveFriendFromTeam,
                teamMembers = teamMembers
            )

            AddPersonIconButton(
                onClick = {
                    setTeamToAddMembers(teamEntity)
                    onShowAddFriendsDialog()
                          },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small_padding))
            )
        }
    }
}

@Composable
private fun TeamDetails(
    teamEntity: TeamEntity,
    setTeamToEdit : (TeamEntity) -> Unit,
    onDeleteTeam : (TeamEntity) -> Unit,
    onShowEditTeamDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
   Box(
       contentAlignment = Alignment.Center,
       modifier = modifier
           .fillMaxWidth()
           .padding(dimensionResource(R.dimen.small_padding))
   ) {
       Text(
           text = teamEntity.name,
           textDecoration = TextDecoration.Underline,
           style = MaterialTheme.typography.headlineLarge
       )

       Row(
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier
               .align(Alignment.CenterEnd)
       ) {
           EditIconButton(onClick = {
               setTeamToEdit(teamEntity)
               onShowEditTeamDialog()
           })
           TrashIconButton(onClick = { onDeleteTeam(teamEntity) })
       }
   }
}

@Composable
private fun TeamMembers(
    onRemoveFriendFromTeam: (FriendEntity) -> Unit,
    teamMembers: List<FriendEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimensionResource(R.dimen.min_height_team_members_column))
    ) {
        teamMembers.forEach {
            TeamMemberCard(
                friendName = it.name,
                onRemoveFriendFromTeam = { onRemoveFriendFromTeam(it) }
            )
        }
    }
}

@Composable
private fun TeamMemberCard(
    friendName: String,
    onRemoveFriendFromTeam : () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = modifier
            .padding(dimensionResource(R.dimen.small_padding))
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = friendName,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                RemovePersonIconButton(onClick = onRemoveFriendFromTeam)
            }
        }
    }
}

@Composable
private fun AddTeamMembersDialog(
    teamEntity: TeamEntity,
    friendListWithoutTeam : List<FriendEntity>,
    onConfirm : (Int, List<Int>) -> Unit,
    onCancel : () -> Unit,
    modifier: Modifier = Modifier
) {
    val listOfAddedFriends = remember {  mutableStateListOf<Int>() }

    Dialog(
        onDismissRequest = onCancel,
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "${stringResource(R.string.t_title_add_friends_to_team_dialog)}\n${teamEntity.name}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.medium_padding))
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f, false)
                ) {
                    items(friendListWithoutTeam) { friend ->

                        val isChecked = listOfAddedFriends.contains(friend.id)

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .padding(
                                    horizontal = dimensionResource(R.dimen.medium_padding),
                                    vertical = dimensionResource(R.dimen.small_padding)
                                )
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            listOfAddedFriends.add(friend.id)
                                        } else {
                                            listOfAddedFriends.remove(friend.id)
                                        }
                                    },
                                )

                                Text(
                                    text = friend.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.medium_padding))
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1.0f)
                    ) {
                        Text(
                            text = stringResource(R.string.f_cancel_friend_button),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = { onConfirm(teamEntity.id, listOfAddedFriends) },
                        enabled = !listOfAddedFriends.isEmpty(),
                        modifier = Modifier.weight(1.0f)
                    ) {
                        Text(
                            text = stringResource(R.string.f_confirm_friend_button),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TeamsScreenPreview(){
    GymcanaMasterTheme{
        TeamsScreenContent(
            teamUiState = TeamUiState(),
            onAddTeam = {},
            onDeleteTeam = {},
            onAddFriendsToTeam = { _, _ -> },
            onShowAddFriendsDialog = {},
            onHideAddFriendsDialog = {},
            onRemoveFriendFromTeam = {},
            setTeamToEdit = {},
            setTeamToAddMembers = {},
            onShowAddTeamDialog = {},
            onHideAddTeamDialog = {},
            onEditTeam = {},
            onShowEditTeamDialog = { },
            onHideEditTeamDialog = { }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TeamsScreenPreviewDark(){
    GymcanaMasterTheme{
        TeamsScreenContent(
            teamUiState = TeamUiState(),
            onAddTeam = {},
            onDeleteTeam = {},
            onAddFriendsToTeam = { _, _ -> },
            onShowAddFriendsDialog = {},
            onHideAddFriendsDialog = {},
            onRemoveFriendFromTeam = {},
            setTeamToEdit = {},
            setTeamToAddMembers = {},
            onShowAddTeamDialog = {},
            onHideAddTeamDialog = {},
            onEditTeam = {},
            onShowEditTeamDialog = { },
            onHideEditTeamDialog = { }
        )
    }
}