package com.example.mc_assignment_sensor.Screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable.Factory
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mc_assignment_sensor.database.Orientation
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import kotlin.math.log

@SuppressLint("UnrememberedMutableState")
@Composable
fun GraphScreen(entries: List<Orientation>, pitch: Float, roll: Float, yaw: Float) {
    Log.d("graphScreenGraph", "Number of entries in the graph: $entries")
    val entriesString = entries.joinToString(separator = "\n") { it.toString() }
    val context = LocalContext.current // Get the current context
    // Only take the last 10 values of the entries list
    val pitchData = remember { mutableStateOf(LineData()) }
    val rollData = remember { mutableStateOf(LineData()) }
    val yawData = remember { mutableStateOf(LineData()) }
    var lastEntries by remember { mutableStateOf(listOf<Orientation>()) }

//    LaunchedEffect(entries) {
//         lastEntries = entries.takeLast(10)
//        if (lastEntries.isNotEmpty()) {
//            pitchData.value = entriesToLineData(lastEntries) { it.pitch }
//            rollData.value = entriesToLineData(lastEntries) { it.roll }
//            yawData.value = entriesToLineData(lastEntries) { it.yaw }
//        }
//    }

    LaunchedEffect(entries) {
        lastEntries = entries.takeLast(10)
        if (lastEntries.isNotEmpty()) {
            pitchData.value = entriesToLineData(lastEntries, { it.pitch }, color = Color.RED)
            rollData.value = entriesToLineData(lastEntries, { it.roll }, color = Color.GREEN)
            yawData.value = entriesToLineData(lastEntries, { it.yaw }, color = Color.BLUE)
        }
    }
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Only display the LineChart if there are last entries
        if (lastEntries.isNotEmpty()) {
            pitchData.let { newpitch ->
                rollData.let { newroll ->
                    yawData.let { newyaw ->
                        AccelerometerAllValues(newpitch, newroll, newyaw, pitch, roll , yaw)
                    }
                }
            }
        }


    }
}

@Composable
fun AccelerometerAllValues(newpitch: MutableState<LineData>, newroll: MutableState<LineData>, newyaw: MutableState<LineData>, pitch: Float, roll: Float, yaw: Float)
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
                Log.d("newValue", "AccelerometerAllValues: newpitch: ${newpitch.value}")

                LineChart(newpitch.value, "Pitch")
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                NewSensorValueText("roll:", roll.toString())
                LineChart(newroll.value, "Roll")
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NewSensorValueText("yaw:", yaw.toString())
                LineChart(newyaw.value, "Yaw")

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
//fun entriesToLineData(entries: List<Orientation>, valueSelector: (Orientation) -> Float): LineData {
//    val lineEntries = entries.mapIndexed { index, orientation ->
//        Entry(index.toFloat(), valueSelector(orientation))
//    }
//    Log.d("LineValues", "entriesToLineData: lineEntries: $lineEntries")
//    val lineDataSet = LineDataSet(lineEntries, "Label").apply {
//        mode = LineDataSet.Mode.CUBIC_BEZIER
//    }
//    Log.d("LineValueDataSet", "entriesToLineData: lineDataSet: $lineDataSet")
//    return LineData(lineDataSet)
//}

// Converts a list of Orientation entries to LineData for a LineChart
fun entriesToLineData(entries: List<Orientation>, valueSelector: (Orientation) -> Float, color: Int): LineData {
    val lineEntries = entries.mapIndexed { index, orientation ->
        Entry(index.toFloat(), valueSelector(orientation))
    }
    val lineDataSet = LineDataSet(lineEntries, "Label").apply {
        mode = LineDataSet.Mode.CUBIC_BEZIER
        this.color = color // Set the color of the line
    }
    return LineData(lineDataSet)
}

//
//@Composable
//fun LineChart(lineData: LineData, description: String) {
//    Log.d("NewLineData", "LineChart: lineData: $lineData")
//    AndroidView({ context ->
//        LineChart(context).apply {
//            this.data = lineData
//            Log.d("LineDataSingle", "LineChart: lineData: $context")
//            this.description.text = description
//            this.description.textColor = Color.BLACK
//            this.description.textSize = 16f
//
//            this.xAxis.textColor = Color.BLACK
//            this.xAxis.textSize = 12f
//            this.xAxis.setDrawGridLines(false)
//
//            this.axisLeft.textColor = Color.BLACK
//            this.axisLeft.textSize = 12f
//            this.axisLeft.setDrawGridLines(false)
//
//            this.axisRight.isEnabled = false
//
//            this.legend.isEnabled = true
//
//            this.setTouchEnabled(true)
//            this.setPinchZoom(true)
//
//            this.setDrawBorders(true)
//            this.setBorderColor(Color.BLACK)
//            this.setBorderWidth(1f)
//
//            this.invalidate()
//        }
//    },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp))
//}
@Composable
fun LineChart(lineData: LineData, description: String) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
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

                this.legend.isEnabled = true

                this.setTouchEnabled(true)
                this.setPinchZoom(true)

                this.setDrawBorders(true)
                this.setBorderColor(Color.BLACK)
                this.setBorderWidth(1f)
            }
        },
        update = { chart ->
            chart.data = lineData
            chart.invalidate() // Redraw the chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}


