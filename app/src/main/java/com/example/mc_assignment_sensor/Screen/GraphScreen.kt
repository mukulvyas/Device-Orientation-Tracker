package com.example.mc_assignment_sensor.Screen

import android.graphics.Color
import android.os.Environment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.util.Log
import com.github.mikephil.charting.data.Entry
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mc_assignment_sensor.database.Orientation
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun GraphScreen(entries: List<Orientation>, pitch: Float, roll: Float, yaw: Float) {
    Log.d("graphScreen", "Number of entries in the database: ${entries.size}")
    val entriesString = entries.joinToString(separator = "\n") { it.toString() }
    val context = LocalContext.current // Get the current context

//    if (entriesString != null) {
//        val file = File(context.getExternalFilesDir(null), "entries.txt")
//
//        // Create the necessary directories
//        file.parentFile?.mkdirs()
//
//        try {
//            FileOutputStream(file).use { outputStream ->
//                OutputStreamWriter(outputStream).use { writer ->
//                    writer.write(entriesString)
//                }
//            }
//        } catch (e: Exception) {
//            // Log the exception
//            Log.e("File Writing Error", "Error writing to file", e)
//        }
//    } else {
//        Log.e("File Writing Error", "entriesString is null")
//    }

    if (entriesString != null) {
        val file = File(context.filesDir, "entries.txt")

        try {
            FileOutputStream(file).use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(entriesString)
                }
            }
        } catch (e: Exception) {
            // Log the exception
            Log.e("File Writing Error", "Error writing to file", e)
        }
    } else {
        Log.e("File Writing Error", "entriesString is null")
    }

//    if (entriesString != null) {
//        val file = File(Environment.getExternalStorageDirectory(), "entries.txt")
//
//        // Create the necessary directories
//        file.parentFile?.mkdirs()
//
//        try {
//            FileOutputStream(file).use { outputStream ->
//                OutputStreamWriter(outputStream).use { writer ->
//                    writer.write(entriesString)
//                }
//            }
//        } catch (e: Exception) {
//            // Log the exception
//            Log.e("File Writing Error", "Error writing to file", e)
//        }
//    } else {
//        Log.e("File Writing Error", "entriesString is null")
//    }


    val pitchData = entriesToLineData(entries.takeLast(10)) { it.pitch }
    val rollData = entriesToLineData(entries.takeLast(10)) { it.roll }
    val yawData = entriesToLineData(entries.takeLast(10)) { it.yaw }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NewSensorValueText("pitch:", pitch.toString())
                    LineChart(newpitch, "Pitch")
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    NewSensorValueText("roll:", roll.toString())
                    LineChart(newroll, "Roll")
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NewSensorValueText("yaw:", yaw.toString())
                    LineChart(newyaw, "Yaw")
                }
            }
        }

    }

@Composable
fun NewSensorValueText(
    label: String,
    value: String
) {
    OutlinedTextField(
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
    val lineDataSet = LineDataSet(lineEntries, "Label").apply {
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
    return LineData(lineDataSet)
}

//@Composable
//fun updateChart(pitchData: LineData, rollData: LineData, yawData: LineData) {
//    LineChart(pitchData, "Pitch")
//    LineChart(rollData, "Roll")
//    LineChart(yawData, "Yaw")
//}



// A Composable that displays a LineChart with the given LineData
//fun LineChart(lineData: LineData, description: String) {
//    AndroidView({ context ->
//        LineChart(context).apply {
//            this.data = lineData
//            this.description.text = description
//            this.invalidate()
//        }
//    }, modifier = Modifier.fillMaxWidth().height(200.dp))
//}
@Composable
fun LineChart(lineData: LineData, description: String) {
    AndroidView({ context ->
        LineChart(context).apply {
            this.data = lineData
            this.description.text = description
            this.description.textColor = Color.BLACK
            this.description.textSize = 16f

            this.xAxis.textColor = Color.BLACK
            this.xAxis.textSize = 12f
            this.xAxis.setDrawGridLines(false)

            this.axisLeft.textColor = Color.BLACK
            this.axisLeft.textSize = 12f
            this.axisLeft.setDrawGridLines(false)

            this.axisRight.isEnabled = false

            this.legend.isEnabled = false

            this.setTouchEnabled(true)
            this.setPinchZoom(true)

            this.setDrawBorders(true)
            this.setBorderColor(Color.BLACK)
            this.setBorderWidth(1f)

            this.invalidate()
        }
    }, modifier = Modifier.fillMaxWidth().height(200.dp))
}