package com.example.gymcanamaster.ui

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gymcanamaster.R
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopAppVBar(
    @StringRes titleResId: Int,
) {
    CenterAlignedTopAppBar(
        title = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
            ) {

                Text(
                    text = stringResource(titleResId),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
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
        )
    )
}

@Composable
fun InputTextDialog(
    initialText: String,
    @StringRes textFieldLabelResId: Int,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf(initialText) }

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
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm(inputText) }
                    ),
                    label = {
                        Text(
                            text = stringResource(textFieldLabelResId),
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
                        onClick = { onConfirm(inputText) },
                        enabled = inputText.isNotBlank(),
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
fun EditIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.edit_icon),
            contentDescription = null
        )
    }
}

@Composable
fun AddPersonIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painterResource(R.drawable.add_friend_icon),
            contentDescription = null
        )
    }
}

@Composable
fun RemovePersonIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painterResource(R.drawable.remove_friend_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun TrashIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painterResource(R.drawable.trash_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painterResource(R.drawable.add_icon),
            contentDescription = null
        )
    }
}

@Composable
fun GameBottomNavigationBar(
    currentGameIndex : Int,
    maxGameIndex : Int,
    isNextButtonEnabled : Boolean,
    onNavigateToPreviousGame : () -> Unit,
    onNavigateToNextGame : () -> Unit,

    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {
            OutlinedButton(
                onClick = onNavigateToPreviousGame,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(
                    text = if(currentGameIndex == 0)
                        stringResource(R.string.g_exit_game_button_text)
                    else
                        stringResource(R.string.g_previous_game_button_text),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Text(
                text = "${currentGameIndex + 1}/${maxGameIndex + 1}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.medium_padding))
            )

            Button(
                onClick = onNavigateToNextGame,
                enabled = isNextButtonEnabled,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(
                    text = if (currentGameIndex != maxGameIndex)
                        stringResource(R.string.g_next_game_button_text)
                    else
                        stringResource(R.string.g_finish_game_button_text),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable private fun TopAppBarPreview(){
    GymcanaMasterTheme {
        TransparentTopAppVBar(R.string.g_game_title_chopstick_transfer)
    }
}