package com.example.gymcanamaster.data.room.friend

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friend: FriendEntity)

    @Delete
    suspend fun delete(friend: FriendEntity)

    @Update
    suspend fun update(friend: FriendEntity)

    @Query("SELECT * FROM friends ORDER BY name ASC")
    fun getAll(): Flow<List<FriendEntity>>
}