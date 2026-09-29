package com.example.gymcanamaster.ui.mainMenu.friends

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymcanamaster.R
import com.example.gymcanamaster.data.AppViewModelProvider
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.ui.AddFloatingActionButton
import com.example.gymcanamaster.ui.EditIconButton
import com.example.gymcanamaster.ui.InputTextDialog
import com.example.gymcanamaster.ui.RemovePersonIconButton
import com.example.gymcanamaster.ui.TransparentTopAppVBar
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
        topBar = { TransparentTopAppVBar(R.string.f_title_friends_screen) },
        containerColor = Color.Transparent,
        floatingActionButton = { AddFloatingActionButton(onClick = { onDisplayAddFriendDialog() }) },
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_padding))
                    .weight(1.0f),
                text = friend.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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

            EditIconButton(onClick = { onEditFriend(friend) })
            RemovePersonIconButton(onClick = { onDeleteFriend(friend) })

        }
    }
}

@Composable
private fun AddFriendDialog(
    onAdd : (String) -> Unit,
    onDismiss : () -> Unit,
    modifier : Modifier = Modifier
){
    InputTextDialog(
        initialText = "",
        textFieldLabelResId = R.string.f_label_add_friend_text_field,
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
    InputTextDialog(
        initialText = currentName,
        textFieldLabelResId = R.string.f_label_add_friend_text_field,
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
                isAddFriendDialogVisible = true
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