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
    state: State
) {
    Button(
        onClick = {
            state.maximizeButton()
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
    state: State
) {
    Button(
        onClick = {
            state.minimizeButton()
        },
        colors = ButtonDefaults.buttonColors(
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
    state: State
) {
    Button(
        onClick = {
            state.incrementButton()
            //stateChanged(state)
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
    state: State
) {
    Button(
        onClick = {
            state.decrementButton()
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
    state: State
) {
    Button(
        onClick = {
            state.incrementButton()
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
    state: State
) {
    Button(
        onClick = {
            state.decrementButton()
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
    label: String,
    state: State,
    offsetX: Int,
    offsetY: Int,
    stateChanged: (State) -> Unit
) {
    val buttonWidthDp = ButtonParameters.buttonWidthsDp[state.getButtonSizeIndex()]
    val buttonHeightDp = ButtonParameters.buttonHeightsDp[state.getButtonSizeIndex()]
    val buttonRoundedSize = ButtonParameters.buttonRoundedSizes[state.getButtonSizeIndex()]
    log(
        "button $buttonNumber gap index ${state.buttonGapPctIndex} label $label " +
                "(${buttonWidthDp}, ${buttonHeightDp}) at (${offsetX}, ${offsetY})"
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
                buttonWidthDp,
                buttonHeightDp
            )
            //.align(Alignment.Center) // Center the button within the Box
            .clip(RoundedCornerShape(buttonRoundedSize))//28.dp)) // Apply rounded corners
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
    val recompose: () -> Unit =
        {
            val buttonSizeIndex = state.getButtonSizeIndex() // for debugging
            // necessary to properly resize buttons after expand, launch, compress, launch
            state.recalculateOffsets()
            stateChanged(state)
        }
    val buttonWidthDp = ButtonParameters.buttonWidthsDp[state.getButtonSizeIndex()]
    val buttonHeightDp = ButtonParameters.buttonHeightsDp[state.getButtonSizeIndex()]
    log(
        "main box index ${state.buttonGapPctIndex} " + "(${buttonWidthDp}, ${buttonHeightDp})"
    )
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
                        val buttonSizeIndex = state.getButtonSizeIndex() // for debugging
                        PointerEvents.onBoxPointerEvent(
                            event,
                            state,
                            stateChanged,
                            recompose
                        )
                    }
                }
            }
    ) {
        val buttonWidthPx =
            ButtonParameters.buttonWidthsPx[state.getButtonSizeIndex()]//.roundToInt()
        val buttonheightPx =
            ButtonParameters.buttonWidthsPx[state.getButtonSizeIndex()]//.roundToInt()
        val boxOffset = state.getBoxOffset()
        val allAppsSorted = state.apps.allApps.toList()
            .sortedWith(
                compareByDescending<App> { it.openCount }
                    .thenByDescending { it.priority }
                    .thenByDescending { it.label }
            ).toMutableList()
        for (screenRow in 0 until state.screenRows) {
            val offsetBox1Y =
                (boxOffset.y + screenRow * buttonheightPx * (state.gapPercentage + 1)).roundToInt()
            for (screenCol in 0 until state.screenCols) {
                val offsetBox1X =
                    (boxOffset.x + screenCol * buttonWidthPx * (state.gapPercentage + 1)).roundToInt()
                var label: String
                var buttonNumber: Int
                if (allAppsSorted.isEmpty()) {
                    buttonNumber = -1
                    label = "unused"
                } else {
                    val app = allAppsSorted.removeAt(0)
                    buttonNumber = app.id
                    label = app.label
                }

                ButtonBox(
                    buttonNumber,
                    label,
                    state,
                    offsetBox1X,
                    offsetBox1Y,
                    stateChanged
                )
            }
        }
    }
}
