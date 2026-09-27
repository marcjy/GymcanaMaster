package com.example.gymcanamaster.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymcanamaster.data.room.friend.FriendDao
import com.example.gymcanamaster.data.room.friend.FriendEntity

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [FriendEntity::class], version = 1, exportSchema = false)
abstract class GymcanaDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile
        private var Instance: GymcanaDatabase? = null

        fun getDatabase(context: Context): GymcanaDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GymcanaDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}