package com.example.gymcanamaster.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymcanamaster.data.room.friend.FriendDao
import com.example.gymcanamaster.data.room.friend.FriendEntity
import com.example.gymcanamaster.data.room.team.TeamDao
import com.example.gymcanamaster.data.room.team.TeamEntity

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [
        FriendEntity::class,
        TeamEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class GymcanaDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao
    abstract fun teamDao(): TeamDao

    companion object {
        @Volatile
        private var Instance: GymcanaDatabase? = null

        fun getDatabase(context: Context): GymcanaDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GymcanaDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}