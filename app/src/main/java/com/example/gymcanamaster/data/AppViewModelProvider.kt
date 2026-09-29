package com.example.gymcanamaster.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymcanamaster.GymcanaMasterApplication
import com.example.gymcanamaster.ui.mainMenu.friends.FriendsViewModel
import com.example.gymcanamaster.ui.teams.TeamsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FriendsViewModel(
                friendRepository = gymcanaMasterApplication().container.friendRepository
            )
        }

        initializer {
            TeamsViewModel(
                teamRepository = gymcanaMasterApplication().container.teamRepository,
                friendRepository = gymcanaMasterApplication().container.friendRepository
            )
        }
    }
}

fun CreationExtras.gymcanaMasterApplication(): GymcanaMasterApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymcanaMasterApplication)