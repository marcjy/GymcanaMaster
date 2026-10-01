package com.example.gymcanamaster.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.gymcanamaster.ui.games.PhysicalGameScreen
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
                onNavigateToStart = {navigateToGame(navController, 0)}
            )
        }

        composable<NavRoute.FriendsAndTeamsScreen> {
            FriendsAndTeamsScreen()
        }

        //Physical games

        composable<NavRoute.PhysicalGameScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.PhysicalGameScreen>()
            val targetGame = gymcanaGames.getOrNull(route.gameId)

            if(targetGame == null)
                throw Exception("When trying to navigate to a ${NavRoute.PhysicalGameScreen::class.simpleName}, the gameId is null")

            PhysicalGameScreen(
                gameTitleResId = targetGame.gameTitleResId,
                currentGameIndex = route.gameId,
                maxGameIndex = gymcanaGames.size - 1,
                isTeamBasedGame = targetGame.isTeamBasedGame,
                onNavigateToPreviousGame = {
                    if(route.gameId > 0)
                        navigateToGame(navController, route.gameId - 1)
                    else
                        navController.popBackStack()
                },
                onNavigateToNextGame ={ navigateToGame(navController, route.gameId + 1) }
            )

        }
    }
}

private fun navigateToGame(
    navController: NavHostController,
    targetGameId : Int
){
    val targetGame = gymcanaGames.getOrNull(targetGameId)

    if(targetGame == null)
        //TODO:Navigate to finish screen
        return

    val targetGameRoute : NavRoute = when(targetGame.gameType){
        GameType.Physical -> NavRoute.PhysicalGameScreen(targetGameId)

    }
    navController.navigate(targetGameRoute){
        popUpTo(NavRoute.MainMenu) {inclusive = true}
    }
}