package com.example.gymcanamaster.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymcanamaster.GymcanaMasterApplication
import com.example.gymcanamaster.ui.mainMenu.friends.FriendsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FriendsViewModel(
                friendRepository = gymcanaMasterApplication().container.friendRepository
            )
        }

    //TODO: Add TeamViewModel
//        initializer {
//            TeamViewModel(
//                teamRepository = gymcanaMasterApplication().container.teamRepository
//            )
//        }
    }
}

fun CreationExtras.gymcanaMasterApplication(): GymcanaMasterApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymcanaMasterApplication)