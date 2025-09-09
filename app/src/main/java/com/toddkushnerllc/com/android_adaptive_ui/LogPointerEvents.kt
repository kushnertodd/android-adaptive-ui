package com.toddkushnerllc.com.android_adaptive_ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
•	The pointerInput modifier. You pass it one or more keys. When the value of one of those keys changes, the modifier content lambda is re-executed. The sample passes an optional filter to the composable. If the value of that filter changes, the pointer event handler should be re-executed to make sure the right events are logged.
•	awaitPointerEventScope creates a coroutine scope that can be used to wait for pointer events.
•	awaitPointerEvent suspends the coroutine until a next pointer event occurs.

*/
@Composable
fun LogPointerEvents(
    buttonWidth: Dp, buttonHeight: Dp, buttonPadding: Dp, filter: PointerEventType? = null
) {
    var log by remember { mutableStateOf("") }
    var buttonPadding by remember { mutableStateOf(16.dp) }
    var buttonSizeIndex by remember { mutableStateOf(0) }
    val buttonSizeIndexMax = 14
    val buttonWidths = arrayOf(
        150.dp,
        165.dp,
        180.dp,
        195.dp,
        210.dp,
        225.dp,
        240.dp,
        255.dp,
        270.dp,
        285.dp,
        300.dp,
        315.dp,
        330.dp,
        345.dp,
        360.dp
    )
    val buttonHeights = arrayOf(
        120.dp,
        132.dp,
        144.dp,
        156.dp,
        168.dp,
        180.dp,
        192.dp,
        204.dp,
        216.dp,
        228.dp,
        240.dp,
        252.dp,
        264.dp,
        276.dp,
        288.dp
    )
    val buttonTextSizes = arrayOf(
        15.sp,
        18.sp,
        21.sp,
        24.sp,
        27.sp,
        30.sp,
        33.sp,
        36.sp,
        39.sp,
        42.sp,
        45.sp,
        48.sp,
        51.sp,
        54.sp,
        57.sp
    )

    val onBoxPointerEvent: (PointerEvent) -> Unit = { event ->
        // Process the PointerEvent here
        //println("Pointer event occurred at: ${event.changes.first().position}")
        var log =
            "box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               "
        Log.d(
            "LogPointerEvents",
            log
        )
//        if (buttonSizeIndex >0)
//            buttonSizeIndex--
    }
    val onButtonPointerEvent: (PointerEvent) -> Unit = { event ->
        // Process the PointerEvent here
        //println("Pointer event occurred at: ${event.changes.first().position}")
        var log =
            "button ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               "
        Log.d(
            "LogPointerEvents",
            log
        )
        if (buttonSizeIndex < buttonSizeIndexMax)
            buttonSizeIndex++
    }

    Column {
        Text(log)
        Box(
            Modifier
                .width(420.dp)
                .height(850.dp)
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(filter) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            // handle pointer event
                            if (filter == null || event.type == filter) {
                                onBoxPointerEvent(event)
                            }
                        }
                    }
                }
        ) {
            // The Button composable placed inside the Box
            Button(
                onClick = { /* Handle button click */ },
                shape = RoundedCornerShape(2.dp),
                modifier = Modifier
                    // Set a specific size for the button
                    .size(
                        buttonWidths[buttonSizeIndex],
                        buttonHeights[buttonSizeIndex]
                    )
                    .align(Alignment.Center) // Center the button within the Box
                    .padding(buttonPadding) // Add some padding around the button
                    .pointerInput(filter) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                // handle pointer event
                                if (filter == null || event.type == filter) {
                                    onButtonPointerEvent(event)
                                }
                            }
                        }
                    }
            ) {
                Text(
                    text = "Click Me",
                    fontSize = buttonTextSizes[buttonSizeIndex]
                )
            }
        }
    }
}
