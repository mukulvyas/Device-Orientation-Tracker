
package com.example.mc_assignment_sensor.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mc_assignment_sensor.Accelerometer.Accelerometer

@Composable
fun AccelerometerScreen(accelerometer: Accelerometer) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AccelerometerValues(
            accelerometerXValues = accelerometer.accelerometerXValues.value,
            accelerometerYValues = accelerometer.accelerometerYValues.value,
            accelerometerZValues = accelerometer.accelerometerZValues.value
        )
    }
}

@Composable
fun AccelerometerValues(
    accelerometerXValues: String,
    accelerometerYValues: String,
    accelerometerZValues: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorValueText("X Value:", accelerometerXValues)
        SensorValueText("Y Value:", accelerometerYValues)
        SensorValueText("Z Value:", accelerometerZValues)
    }
}

@Composable
fun SensorValueText(
    label: String,
    value: String
) {
    TextField(
        value = value,
        onValueChange = {},
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}