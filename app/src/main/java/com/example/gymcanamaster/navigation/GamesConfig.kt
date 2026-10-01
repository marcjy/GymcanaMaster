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
    GameData(R.string.g_game_title_keep_up_ballon, GameType.Physical, true),
    GameData(R.string.g_game_title_race_running, GameType.Physical, true),
    GameData(R.string.g_game_title_egg_race, GameType.Physical, true),
    GameData(R.string.g_game_title_ballon_race, GameType.Physical, true),
    GameData(R.string.g_game_title_ballon_survival, GameType.Physical, false),
    GameData(R.string.g_game_title_three_legged_race, GameType.Physical, true),
    GameData(R.string.g_game_title_scarf_game, GameType.Physical, true),
    GameData(R.string.g_game_title_human_conveyor_belt, GameType.Physical, true),
    GameData(R.string.g_game_title_rock_paper_scissors_survival, GameType.Physical, false),
    GameData(R.string.g_game_title_scavenger_hunt, GameType.Physical, true),
    GameData(R.string.g_game_title_chopstick_transfer, GameType.Physical, true),
    GameData(R.string.g_game_title_beer_pong, GameType.Physical, true),
    GameData(R.string.g_game_title_cup_pyramid, GameType.Physical, true)

)
