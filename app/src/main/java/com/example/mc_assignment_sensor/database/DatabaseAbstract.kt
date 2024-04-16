//package com.example.mc_assignment_sensor.database
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//
//@Database(entities = [Orientation::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun sensorDataDao(): OrientationDao
//}


// AppDatabase.kt
package com.example.mc_assignment_sensor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Orientation::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orientationDao(): OrientationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}