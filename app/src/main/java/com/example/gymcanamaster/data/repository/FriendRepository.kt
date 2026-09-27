package com.example.gymcanamaster.data.repository

import com.example.gymcanamaster.data.room.friend.FriendEntity
import kotlinx.coroutines.flow.Flow

interface FriendRepository {

    suspend fun createFriend(friend : FriendEntity)
    suspend fun deleteFriend(friend: FriendEntity)
    suspend fun updateFriend(friend: FriendEntity)

    fun getAllFriends(): Flow<List<FriendEntity>>
}