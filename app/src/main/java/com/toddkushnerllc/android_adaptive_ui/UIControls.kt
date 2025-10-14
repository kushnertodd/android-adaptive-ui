package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
import kotlin.math.roundToInt

@Composable
fun ConfirmButtonTapDialog(
    state: State
) {
    AlertDialog(
        onDismissRequest = state.onDismiss, // Called when the user dismisses the dialog (e.g., taps outside)
        title = { Text(text = "Confirmation", color = MaterialTheme.colorScheme.primary) },
        text = { Text(text = "Ok to run command", color = MaterialTheme.colorScheme.secondary) },
        confirmButton = {
            TextButton(onClick = { state.onConfirm() }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { state.onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MaximizeButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.maximizeButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u2191",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        /*
                Image(
                    painter = painterResource(id = R.drawable.arrow_upward_24dp),//48dp), // Assuming "my_image.png" was imported
                    contentDescription = "Maximize button"
                )
        */
    }
}

@Composable
fun MinimizeButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.minimizeButton()
            stateChanged(state)
        }, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u2193",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        /*
                Image(
                    painter = painterResource(id = R.drawable.arrow_downward_24dp),//48dp), // Assuming "my_image.png" was imported
                    contentDescription = "Reset button"
                )
        */
    }
}

@Composable
fun IncrementButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.incrementButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u002B", color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 36.sp
        )
        /*
                Image(
                    painter = painterResource(id = R.drawable.arrow_plus_24dp),//48dp), // Assuming "my_image.png" was imported
                    contentDescription = "Grow button"
                )
        */
    }
}

@Composable
fun DecrementButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.decrementButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u002D", color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 36.sp
        )
        /*
                Image(
                    painter = painterResource(id = R.drawable.arrow_minus_24dp),//48dp), // Assuming "my_image.png" was imported
                    contentDescription = "Shrink button"
                )
        */
    }
}

@Composable
fun ExpandButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.incrementButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u02c2 \u02c3", color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 30.sp
        )
        /*
        Image(
            painter = painterResource(id = R.drawable.arrow_expand_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Shrink button"
        )
*/
    }
}

@Composable
fun CompressButton(
    state: State,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            state.decrementButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary  // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u02c3 \u02c2", color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 30.sp
        )
        /*
                Image(
                    painter = painterResource(id = R.drawable.arrow_compress_24dp),//48dp), // Assuming "my_image.png" was imported
                    contentDescription = "Shrink button"
                )
        */
    }
}

@Composable
fun ButtonBox(
    buttonNumber: Int,
    state: State,
    label: String,
    offsetX: Int,
    offsetY: Int,
    stateChanged: (State) -> Unit
) {
    //var ct by remember { mutableStateOf(0) }
    log(
        "button ${buttonNumber} gap index ${state.buttonGapPctIndex} label ${label} " +
                "(${ButtonParameters.buttonWidthsDp[state.getButtonSizeIndex()]}, ${ButtonParameters.buttonHeightsDp[state.getButtonSizeIndex()]}) at (${offsetX}, ${offsetY})"
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset {
                IntOffset(
                    offsetX,
                    offsetY
                )
            }
            .size(
                ButtonParameters.buttonWidthsDp[state.getButtonSizeIndex()],
                ButtonParameters.buttonHeightsDp[state.getButtonSizeIndex()]
            )
            //.align(Alignment.Center) // Center the button within the Box
            .clip(RoundedCornerShape(ButtonParameters.buttonRoundedSizes[state.getButtonSizeIndex()]))//28.dp)) // Apply rounded corners
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(
                // necessary, not Unit, the secret sauce to get pointerInput to reinitialize
                state.getCounter()
            ) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        PointerEvents.onButtonPointerEvent(
                            buttonNumber,
                            label,
                            event,
                            state,
                            stateChanged
                        )
                        //ct++
                    }
                }
            }
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = ButtonParameters.buttonTextSizes[state.getButtonSizeIndex()]
        )
    }
}

@Composable
fun MainBox(
    density: Density,
    state: State,
    stateChanged: (State) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .onGloballyPositioned { coordinates ->
                state.setBox(
                    Dimensions(
                        Extent.pxToExtent(density, coordinates.size.width.toFloat()),
                        Extent.pxToExtent(density, coordinates.size.height.toFloat()),
                    )
                )
            }
            .pointerInput(
                Unit
            ) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        PointerEvents.onBoxPointerEvent(
                            event,
                            state,
                            stateChanged
                        )
                    }
                }
            }
    ) {
        val buttonWidth =
            with(density) {
                ButtonParameters.buttonWidthsPx[state.getButtonSizeIndex()]//.roundToInt()
            }
        val buttonheight =
            with(density) {
                ButtonParameters.buttonWidthsPx[state.getButtonSizeIndex()]//.roundToInt()
            }
        val boxOffset = state.getBoxOffset()
        for (screenRow in 0 until state.screenRows) {
            val offsetBox1Y =
                (boxOffset.y + screenRow * buttonheight * (state.gapPercentage + 1)).roundToInt()
            val sortedApps = state.apps.allApps.sorted()
            for (screenCol in 0 until state.screenCols) {
                val offsetBox1X =
                    (boxOffset.x + screenCol * buttonWidth * (state.gapPercentage + 1)).roundToInt()
                val buttonNumber = screenCol + (screenRow * state.screenCols)
                var label = ""
                val app = Apps.findAppById(buttonNumber, sortedApps)
                if (app == null) {
                    label = "unused"
                } else {
                    label = app.label
                }

                ButtonBox(
                    buttonNumber,
                    state,
                    label,
                    offsetBox1X,
                    offsetBox1Y,
                    stateChanged
                )
            }
        }
    }
}
