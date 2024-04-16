package com.example.mc_assignment_sensor.Screen

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.util.Log
import com.github.mikephil.charting.data.Entry
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mc_assignment_sensor.database.Orientation

@Composable
fun GraphScreen(entries: List<Orientation>, pitch: Float, roll: Float, yaw: Float) {
    Log.d("graphScreen", "Number of entries in the database: ${entries.size}")

//    val pitchData = entriesToLineData(entries) { it.pitch }
//    val rollData = entriesToLineData(entries) { it.roll }
//    val yawData = entriesToLineData(entries) { it.yaw }
    val pitchData = entriesToLineData(entries.take(50)) { it.pitch }
    val rollData = entriesToLineData(entries.take(50)) { it.roll }
    val yawData = entriesToLineData(entries.take(50)) { it.yaw }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AccelerometerAllValues(pitchData, rollData, yawData,
            pitch = pitch,
            roll = roll,
            yaw = yaw )

    }
}

@Composable
fun AccelerometerAllValues(newpitch: LineData, newroll: LineData, newyaw: LineData, pitch: Float, roll: Float, yaw: Float)
    {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NewSensorValueText("pitch:", pitch.toString())
            LineChart(newpitch, "Pitch")

        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NewSensorValueText("roll:", roll.toString())
            LineChart(newroll, "Roll")

        }
        Column(
            modifier = Modifier.fillMaxWidth(),
           // horizontalArrangement = Arrangement.SpaceBetween
        ) {

            NewSensorValueText("yaw:", yaw.toString())
            LineChart(newyaw, "Yaw")

        }
    }
}

@Composable
fun NewSensorValueText(
    label: String,
    value: String
) {
    TextField(
        value = value,
        onValueChange = {},
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.titleMedium,
        //modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    )
}


// Converts a list of Orientation entries to LineData for a LineChart
fun entriesToLineData(entries: List<Orientation>, valueSelector: (Orientation) -> Float): LineData {
    val lineEntries = entries.mapIndexed { index, orientation ->
        Entry(index.toFloat(), valueSelector(orientation))
    }
    val lineDataSet = LineDataSet(lineEntries, "Label")
    return LineData(lineDataSet)
}

// A Composable that displays a LineChart with the given LineData
@Composable
fun LineChart(lineData: LineData, description: String) {
    AndroidView({ context ->
        LineChart(context).apply {
            this.data = lineData
            this.description.text = description
            this.invalidate()
        }
    }, modifier = Modifier.fillMaxWidth().height(200.dp))
}