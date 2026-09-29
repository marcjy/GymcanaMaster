package com.example.gymcanamaster.data

import android.content.Context
import com.example.gymcanamaster.data.repository.friend.FriendRepository
import com.example.gymcanamaster.data.repository.friend.RoomFriendRepository
import com.example.gymcanamaster.data.repository.team.RoomTeamRepository
import com.example.gymcanamaster.data.repository.team.TeamRepository
import com.example.gymcanamaster.data.room.GymcanaDatabase


interface AppContainer{
    val friendRepository: FriendRepository
    val teamRepository : TeamRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val friendRepository: FriendRepository by lazy {
        RoomFriendRepository(GymcanaDatabase.getDatabase(context).friendDao())
    }

    override val teamRepository: TeamRepository by lazy {
        RoomTeamRepository(GymcanaDatabase.getDatabase(context).teamDao())
    }
}