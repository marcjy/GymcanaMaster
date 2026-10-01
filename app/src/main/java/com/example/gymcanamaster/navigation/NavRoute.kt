package com.example.gymcanamaster.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {

    @Serializable
    data object MainMenu : NavRoute

    @Serializable
    data object FriendsAndTeamsScreen : NavRoute
    @Serializable
    data object FriendsScreen : NavRoute
    @Serializable
    data object TeamsScreen : NavRoute


    //Games
    @Serializable
    data class PhysicalGameScreen(val gameId : Int) : NavRoute
}