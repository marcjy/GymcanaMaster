package com.example.gymcanamaster.data.room.team

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.gymcanamaster.data.room.relations.TeamWithFriends
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(team: TeamEntity)

    @Update
    suspend fun update(team: TeamEntity)

    @Delete
    suspend fun delete(team: TeamEntity)

    @Query("SELECT * FROM teams ORDER BY name ASC")
    fun getAll(): Flow<List<TeamEntity>>

    @Transaction
    @Query("SELECT * FROM teams")
    fun getAllTeamsWithFriends(): Flow<List<TeamWithFriends>>

}