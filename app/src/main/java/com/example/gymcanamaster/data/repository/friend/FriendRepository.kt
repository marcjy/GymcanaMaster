package com.example.gymcanamaster.data.repository.friend

import com.example.gymcanamaster.data.room.friend.FriendEntity
import kotlinx.coroutines.flow.Flow

interface FriendRepository {

    suspend fun createFriend(friend : FriendEntity)
    suspend fun deleteFriend(friend: FriendEntity)
    suspend fun updateFriend(friend: FriendEntity)
    suspend fun updateFriends(friends: List<FriendEntity>)
    suspend fun addFriendsToTeam(teamId : Int, friendIds : List<Int>)

    fun getAllFriends(): Flow<List<FriendEntity>>
}