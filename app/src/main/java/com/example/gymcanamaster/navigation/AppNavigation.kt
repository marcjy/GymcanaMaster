package com.example.gymcanamaster.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymcanamaster.ui.mainMenu.MainMenu
import com.example.gymcanamaster.ui.mainMenu.friends.FriendsScreen
import com.example.gymcanamaster.ui.teams.TeamsScreen

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
                onNavigateToFriends = {
                    navController.navigate(NavRoute.FriendsScreen)
                },
                onNavigateToTeams = {
                    navController.navigate(NavRoute.TeamsScreen)
                }
            )
        }

        composable<NavRoute.FriendsScreen> {
            FriendsScreen()
        }


        composable<NavRoute.TeamsScreen> {
            TeamsScreen()
        }
    }

}