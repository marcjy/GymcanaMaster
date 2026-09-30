package com.example.gymcanamaster.data.repository.friend

import com.example.gymcanamaster.data.room.friend.FriendDao
import com.example.gymcanamaster.data.room.friend.FriendEntity
import kotlinx.coroutines.flow.Flow

class RoomFriendRepository(private val friendDao: FriendDao) : FriendRepository {

    override suspend fun createFriend(friend: FriendEntity) = friendDao.insert(friend)
    override suspend fun deleteFriend(friend: FriendEntity) = friendDao.delete(friend)
    override suspend fun updateFriend(friend: FriendEntity) = friendDao.update(friend)
    override suspend fun updateFriends(friends: List<FriendEntity>) = friendDao.update(friends)

    override suspend fun addFriendsToTeam(teamId: Int, friendIds: List<Int>) = friendDao.addFriendsToTeam(teamId, friendIds)

    override fun getAllFriends(): Flow<List<FriendEntity>> = friendDao.getAll()
}