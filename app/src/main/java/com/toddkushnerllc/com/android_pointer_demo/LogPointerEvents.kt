package com.toddkushnerllc.com.android_pointer_demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/*
•	The pointerInput modifier. You pass it one or more keys. When the value of one of those keys changes, the modifier content lambda is re-executed. The sample passes an optional filter to the composable. If the value of that filter changes, the pointer event handler should be re-executed to make sure the right events are logged.
•	awaitPointerEventScope creates a coroutine scope that can be used to wait for pointer events.
•	awaitPointerEvent suspends the coroutine until a next pointer event occurs.

*/
@Composable
fun LogPointerEvents(filter: PointerEventType? = null) {
    var log by remember { mutableStateOf("") }
    Column {
        Text(log)
        Box(
            Modifier
                .size(200.dp)
                .background(Color.Red)
                .pointerInput(filter) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            // handle pointer event
                            if (filter == null || event.type == filter) {
                                log =
                                    "box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               "
                                Log.d(
                                    "LogPointerEvents",
                                    log
                                )
                            }
                        }
                    }
                }
        ) {
            // The Button composable placed inside the Box
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .size(150.dp, 100.dp) // Set a specific size for the button
                    .align(Alignment.Center) // Center the button within the Box
                    .padding(16.dp) // Add some padding around the button
                    .pointerInput(filter) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                // handle pointer event
                                if (filter == null || event.type == filter) {
                                    log =
                                        "button ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               "
                                    Log.d(
                                        "LogPointerEvents",
                                        log
                                    )
                                }
                            }
                        }
                    }
            ) {
                Text("Click Me")
            }
        }
    }
}
