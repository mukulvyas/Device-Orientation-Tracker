package com.example.mc_assignment_sensor.Accelerometer


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateOf
//
//class Accelerometer(context: Context) {
//    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//    var accelerometerXValues = mutableStateOf("")
//    var accelerometerYValues = mutableStateOf("")
//    var accelerometerZValues = mutableStateOf("")
//
//    private val sensorEventListener = object : SensorEventListener {
//        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//            // Handle accuracy changes if needed
//        }
//
//        override fun onSensorChanged(event: SensorEvent?) {
//            event?.let {
//                val x = it.values[0]
//                val y = it.values[1]
//                val z = it.values[2]
//                accelerometerXValues.value = "$x"
//                accelerometerYValues.value = "$y"
//                accelerometerZValues.value = "$z"
//            }
//        }
//    }
//
//    fun startListening() {
//        accelerometer?.let { sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL) }
//    }
//
//    fun stopListening() {
//        sensorManager.unregisterListener(sensorEventListener)
//    }
//}


class Accelerometer(context: Context) {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var accelerometerXValues = mutableStateOf("")
    var accelerometerYValues = mutableStateOf("")
    var accelerometerZValues = mutableStateOf("")
    //private val handler = Handler(Looper.getMainLooper())
    var lastUpdate: Long = 0


    private var sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes if needed
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastUpdate) > 1000) {
                lastUpdate = currentTime
                event?.let {
                    val pitch = it.values[0]
                    val roll = it.values[1]
                    val yaw = it.values[2]
                    accelerometerXValues.value = "$pitch"
                    accelerometerYValues.value = "$roll"
                    accelerometerZValues.value = "$yaw"
                }
            }
        }
    }


    fun startListening() {
        accelerometer?.let { sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }

}
