package com.example.gymcanamaster.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymcanamaster.ui.mainMenu.MainMenu
import com.example.gymcanamaster.ui.mainMenu.friendsAndTeams.FriendsAndTeamsScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.MainMenu,
        modifier = modifier
    ){
        composable<NavRoute.MainMenu>{
            MainMenu(
                onNavigateToFriendsAndTeams = {
                    navController.navigate(NavRoute.FriendsAndTeamsScreen)
                },
                onNavigateToStart = {} //TODO
            )
        }

        composable<NavRoute.FriendsAndTeamsScreen> {
            FriendsAndTeamsScreen()
        }
    }

}