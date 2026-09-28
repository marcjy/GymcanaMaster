package com.example.gymcanamaster.data

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymcanamaster.GymcanaMasterApplication
import com.example.gymcanamaster.ui.mainMenu.friends.FriendsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FriendsViewModel(
                GymcanaMasterApplication().container.friendRepository
            )
        }
    }
}