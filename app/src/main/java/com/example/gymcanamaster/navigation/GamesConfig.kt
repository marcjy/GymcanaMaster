package com.example.gymcanamaster.navigation

import androidx.annotation.StringRes
import com.example.gymcanamaster.R

sealed interface GameType{
    data object Physical : GameType
}

data class GameData(
    @StringRes val gameTitleResId : Int,
    val gameType : GameType,
    val isTeamBasedGame : Boolean
)

val gymcanaGames = listOf(
    GameData(
        gameTitleResId = R.string.g_game_title_chopstick_transfer,
        gameType = GameType.Physical,
        isTeamBasedGame = true
    )
)
