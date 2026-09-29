package com.example.gymcanamaster.ui.mainMenu.friendsAndTeams

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymcanamaster.R
import com.example.gymcanamaster.navigation.NavRoute
import com.example.gymcanamaster.ui.mainMenu.friends.FriendsScreen
import com.example.gymcanamaster.ui.mainMenu.teams.TeamsScreen

@Composable
fun FriendsAndTeamsScreen(
    modifier: Modifier = Modifier
) {
    val innerNavController : NavHostController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar {
                val isFriendsSelected = currentRoute?.contains("FriendsScreen") == true
                //Friends
                NavigationBarItem(
                    selected = isFriendsSelected,
                    onClick = {
                        if(!isFriendsSelected){
                            innerNavController.navigate(NavRoute.FriendsScreen){
                                popUpTo(innerNavController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.friend_icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.ft_label_friends),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )

                val isTeamsSelected = currentRoute?.contains("TeamsScreen") == true
                //Teams
                NavigationBarItem(
                    selected = isTeamsSelected,
                    onClick = {
                        if (!isTeamsSelected) {
                            innerNavController.navigate(NavRoute.TeamsScreen) {
                                popUpTo(innerNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.team_icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.ft_label_teams),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = innerNavController,
            startDestination = NavRoute.FriendsScreen,
            modifier = Modifier
                .padding(it)
        ){
            composable<NavRoute.FriendsScreen> {
                FriendsScreen()
            }
            composable<NavRoute.TeamsScreen> {
                TeamsScreen()
            }
        }
    }
}
