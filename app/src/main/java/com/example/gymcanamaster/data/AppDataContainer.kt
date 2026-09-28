package com.example.gymcanamaster.data

import android.content.Context
import com.example.gymcanamaster.data.repository.FriendRepository
import com.example.gymcanamaster.data.repository.RoomFriendRepository
import com.example.gymcanamaster.data.room.GymcanaDatabase


interface AppContainer{
    val friendRepository: FriendRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val friendRepository: FriendRepository by lazy {
        RoomFriendRepository(GymcanaDatabase.getDatabase(context).friendDao())
    }
}