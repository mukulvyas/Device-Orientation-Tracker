package com.example.mc_assignment_sensor.database



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Orientation(
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    val timestamp: Long,
    val pitch: Float,
    val roll: Float,
    val yaw: Float
)