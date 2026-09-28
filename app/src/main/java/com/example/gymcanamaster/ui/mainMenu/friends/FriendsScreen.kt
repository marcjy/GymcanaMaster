package com.example.gymcanamaster.ui.mainMenu.friends

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymcanamaster.R
import com.example.gymcanamaster.data.AppViewModelProvider
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme


@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    friendsViewModel: FriendsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val friendsUiState by friendsViewModel.uiState.collectAsStateWithLifecycle()

    FriendsScreenContent(
        uiState = friendsUiState,

        onDisplayAddFriendDialog = friendsViewModel::enableAddFriendDialog,
        onAddNewFriend = friendsViewModel::addFriend,
        onDismissAddFriendDialog = friendsViewModel::disableAddFriendDialog,

        setFriendToEdit = friendsViewModel::setFriendToEdit,
        onEditFriend = friendsViewModel::editFriend,
        onDismissEditFriendDialog = friendsViewModel::disableEditFriendDialog,

        onDeleteFriend = friendsViewModel::deleteFriend,

        modifier = modifier
    )
}

@Composable
fun FriendsScreenContent(
    uiState: FriendUiState,

    onDisplayAddFriendDialog : () -> Unit,
    onAddNewFriend : (FriendEntity) -> Unit,
    onDismissAddFriendDialog : () -> Unit,

    setFriendToEdit : (FriendEntity) -> Unit,
    onEditFriend : (FriendEntity) -> Unit,
    onDismissEditFriendDialog : () -> Unit,

    onDeleteFriend: (FriendEntity) -> Unit,

    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {TopFriendBar()},
        containerColor = Color.Transparent,
        modifier = modifier
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
                modifier = Modifier
                    .weight(1.0f)
                    .padding(dimensionResource(R.dimen.small_padding))
            ) {
                items(uiState.friendList) { friend ->
                    FriendCard(
                        friend = friend,
                        onEditFriend = { setFriendToEdit(it)  },
                        onToggleFriendPlaying = { friend, isPlaying ->
                            onEditFriend(friend.copy(isPlaying = isPlaying))
                        },
                        onDeleteFriend = { onDeleteFriend(friend) }
                    )
                }
            }

            FloatingActionButton(
                onClick = { onDisplayAddFriendDialog()},
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_padding))
            ) {
                Icon(
                    painterResource(R.drawable.add_friend_icon),
                    contentDescription = null
                )
            }

            if(uiState.isAddFriendDialogVisible){
                AddFriendDialog(
                    onAdd = { newFriendName ->
                        onAddNewFriend(FriendEntity(name = newFriendName))
                    },
                    onDismiss = {onDismissAddFriendDialog()}
                )
            }

            uiState.friendToEdit?.let { selectedFriend ->
                EditFriendDialog(
                    currentName = selectedFriend.name,
                    onConfirm = { newFriendName ->
                        onEditFriend(selectedFriend.copy(name = newFriendName))
                    },
                    onDismiss = {onDismissEditFriendDialog()}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopFriendBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.f_title_friends_screen),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.huge_padding),
                            vertical = dimensionResource(R.dimen.small_padding)
                        )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
private fun FriendCard(
    friend: FriendEntity,
    onEditFriend : (FriendEntity) -> Unit,
    onToggleFriendPlaying : (FriendEntity, Boolean) -> Unit,
    onDeleteFriend: (FriendEntity) -> Unit,
    modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_padding)),
                text = friend.name,
                style = MaterialTheme.typography.bodyLarge
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small_padding))
            ) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    onClick = { onToggleFriendPlaying(friend, true) },
                    selected = friend.isPlaying,
                    label = {
                        Text(
                            text = stringResource(R.string.f_friend_playing_switch),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )

                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    onClick = { onToggleFriendPlaying(friend, false) },
                    selected = !friend.isPlaying,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.errorContainer,
                        activeContentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    icon = {},
                    label = {
                        Text(
                            text = stringResource(R.string.f_friend_not_playing_switch),
                            style = MaterialTheme.typography.labelMedium,

                        )
                    }
                )
            }


            Spacer(modifier = Modifier.weight(1.0f))

            IconButton(
                onClick = { onEditFriend(friend) }
            ) {
                Icon(
                    painterResource(R.drawable.edit_icon),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = { onDeleteFriend(friend) }
            ) {
                Icon(
                    painterResource(R.drawable.remove_friend_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@Composable
private fun FriendInputDialog(
    initialName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var friendName by remember { mutableStateOf(initialName) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.big_padding))
            ) {
                OutlinedTextField(
                    value = friendName,
                    onValueChange = { friendName = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.f_label_add_friend_text_field),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(R.dimen.big_padding))
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1.0f)
                    ) {
                        Text(
                            text = stringResource(R.string.f_cancel_friend_button),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = { onConfirm(friendName) },
                        enabled = friendName.isNotBlank(),
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

@Composable
private fun AddFriendDialog(
    onAdd : (String) -> Unit,
    onDismiss : () -> Unit,
    modifier : Modifier = Modifier
){
    FriendInputDialog(
        initialName = "",
        onConfirm = onAdd,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
private  fun EditFriendDialog(
    currentName : String,
    onConfirm : (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
){
    FriendInputDialog(
        initialName = currentName,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        modifier = modifier
    )
}


@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FriendsScreenPreviewDark(){
    GymcanaMasterTheme {
        FriendsScreenContent(
            uiState = FriendUiState(
                friendList = listOf(
                    FriendEntity(id = 1, name = "Alex", isPlaying = true),
                    FriendEntity(id = 2, name = "Sarah", isPlaying = false)
                )
            ),
            onDisplayAddFriendDialog = {},
            onAddNewFriend = {},
            onDismissAddFriendDialog = {},
            setFriendToEdit = {},
            onEditFriend = {},
            onDismissEditFriendDialog = {},
            onDeleteFriend = {}
        )
    }
}


@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun FriendsScreenPreview(){
    GymcanaMasterTheme {
        FriendsScreenContent(
            uiState = FriendUiState(
                friendList = listOf(
                    FriendEntity(id = 1, name = "Alex", isPlaying = true),
                    FriendEntity(id = 2, name = "Sarah", isPlaying = false)
                )
            ),
            onDisplayAddFriendDialog = {},
            onAddNewFriend = {},
            onDismissAddFriendDialog = {},
            setFriendToEdit = {},
            onEditFriend = {},
            onDismissEditFriendDialog = {},
            onDeleteFriend = {}
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun FriendsScreenAddDialogPreview() {
    GymcanaMasterTheme {
        FriendsScreenContent(
            uiState = FriendUiState(
                friendList = emptyList(),
                isAddFriendDialogVisible = true // Preview the open dialog!
            ),
            onDisplayAddFriendDialog = {},
            onAddNewFriend = {},
            onDismissAddFriendDialog = {},
            setFriendToEdit = {},
            onEditFriend = {},
            onDismissEditFriendDialog = {},
            onDeleteFriend = {}
        )
    }
}