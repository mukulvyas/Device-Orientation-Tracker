package com.example.mc_assignment_sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mc_assignment_sensor.Screen.GraphScreen
import com.example.mc_assignment_sensor.database.AppDatabase
import com.example.mc_assignment_sensor.database.Orientation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class OrientationActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var orientationSensor: Sensor? = null
    var lastUpdate: Long = 0
    var entries: List<Orientation>? = null
    //private var idCounter = 0
    var orientation by mutableStateOf<Orientation?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        runBlocking {
            entries = withContext(Dispatchers.IO) {
                val orientationDao = AppDatabase.getDatabase(this@OrientationActivity).orientationDao()
                val allEntries = orientationDao.getAll()
                allEntries
            }
           // graphScreen(this@OrientationActivity)
        }

        setContent {
            entries?.let {
                DisplayGraph(it) // Replace 0f with actual pitch, roll and yaw values
            }
        }

    }

    override fun onResume() {
        super.onResume()
        orientationSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        entries?.let {
            Log.d("DatabaseCheckOutsideFunction", "Number of entries in the database: ${it.size}")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    private var idCounter = 0

    override fun onSensorChanged(event: SensorEvent) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastUpdate > 1000) {
        lastUpdate = currentTime
        event.let {
             orientation = Orientation(
                timestamp = lastUpdate,
                pitch = event.values[0],
                roll = event.values[1],
                yaw = event.values[2]
            )

            runBlocking {
                launch(Dispatchers.IO) {
                    val orientationDao = AppDatabase.getDatabase(this@OrientationActivity).orientationDao()
                    orientationDao.insert(orientation!!)
                    Log.d("DatabaseInsert", "Inserted new orientation entry: $orientation")
                    val rowId = orientationDao.insert(orientation!!)
                    Log.d("DatabaseInsertROW", "Inserted new orientation entry with rowId: $rowId")

                    val entries = orientationDao.getAll()
                    Log.d("DatabaseCheck", "Number of entries in the database: $entries")
                    //GraphScreen(entries, event.values[0], event.values[1], event.values[2])

                }
            }
        }
    }
}

    @Composable
    fun DisplayGraph(entries: List<Orientation>) {
        val pitch = orientation?.pitch ?: 0f
        val roll = orientation?.roll ?: 0f
        val yaw = orientation?.yaw ?: 0f

        GraphScreen(entries, pitch, roll, yaw)
    }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // Handle accuracy changes
        }

    }
