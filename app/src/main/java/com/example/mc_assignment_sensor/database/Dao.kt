// Step 2: Create the OrientationDao interface
package com.example.mc_assignment_sensor.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrientationDao {
    @Insert
    suspend fun insert(orientation: Orientation): Long

    @Query("SELECT * FROM orientation")
    suspend fun getAll(): List<Orientation>

    @Query("SELECT * FROM orientation")
    fun getAllFlow(): Flow<List<Orientation>>
}