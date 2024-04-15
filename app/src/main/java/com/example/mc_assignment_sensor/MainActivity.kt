package com.example.mc_assignment_sensor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mc_assignment_sensor.Accelerometer.Accelerometer
import com.example.mc_assignment_sensor.Screen.AccelerometerScreen
import com.example.mc_assignment_sensor.ui.theme.MC_Assignment_SensorTheme

class MainActivity : ComponentActivity() {
    private lateinit var accelerometer: Accelerometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accelerometer = Accelerometer(this)

        setContent {
            MC_Assignment_SensorTheme {
                AccelerometerScreen(accelerometer)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer.startListening()
    }

    override fun onPause() {
        super.onPause()
        accelerometer.stopListening()
    }
}