//package com.example.mc_assignment_sensor
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.example.mc_assignment_sensor.Accelerometer.Accelerometer
//import com.example.mc_assignment_sensor.Screen.AccelerometerScreen
//import com.example.mc_assignment_sensor.ui.theme.MC_Assignment_SensorTheme
//
//class MainActivity : ComponentActivity() {
//    private lateinit var accelerometer: Accelerometer
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        accelerometer = Accelerometer(this)
//
//        setContent {
//            MC_Assignment_SensorTheme {
//                AccelerometerScreen(accelerometer)
//            }
//        }
//        val intent = Intent(this, OrientationActivity::class.java)
//        startActivity(intent)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        accelerometer.startListening()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        accelerometer.stopListening()
//    }
//}

package com.example.mc_assignment_sensor

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mc_assignment_sensor.Accelerometer.Accelerometer
import com.example.mc_assignment_sensor.Screen.AccelerometerScreen
import com.example.mc_assignment_sensor.ui.theme.MC_Assignment_SensorTheme

class MainActivity : ComponentActivity() {
    private lateinit var accelerometer: Accelerometer
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accelerometer = Accelerometer(this)

        setContent {
            MC_Assignment_SensorTheme {
                AccelerometerScreen(accelerometer)
                NextButton()
            }
        }



    }

    @Composable
    fun NextButton() {

        Button(modifier = Modifier.padding(130.dp),

            onClick = {
            val intent = Intent(this, OrientationActivity::class.java)
            startActivity(intent)
        }) {
            Text("New Activity")
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