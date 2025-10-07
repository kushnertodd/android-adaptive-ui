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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
import kotlin.math.roundToInt

@Composable
fun ConfirmButtonTapDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Called when the user dismisses the dialog (e.g., taps outside)
        title = { Text(text = "Confirmation", color = MaterialTheme.colorScheme.primary) },
        text = { Text(text = "Ok to run command", color = MaterialTheme.colorScheme.secondary) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MaximizeButton(
    state: State,
    maximizeButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            maximizeButton()
            stateChanged(state)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u2191", color = MaterialTheme.colorScheme.onPrimary,
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
    minimizeButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            minimizeButton()
            stateChanged(state)
        }, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Sets the background color of the button
            contentColor = MaterialTheme.colorScheme.onTertiary // Sets the color of the text/content inside the button
        )
    ) {
        Text(
            text = "\u2193",
            color = MaterialTheme.colorScheme.onPrimary,
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
    incrementButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            incrementButton()
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
    decrementButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            decrementButton()
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
    expandButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            expandButton()
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
    compressButton: () -> Unit,
    stateChanged: (State) -> Unit
) {
    Button(
        onClick = {
            compressButton()
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
    filter: PointerEventType? = null,
    label: String,
    offsetX: Int,
    offsetY: Int,
    stateChanged: (State) -> Unit
) {
    log("button ${buttonNumber} index ${state.buttonSizeIndex} (${state.getButtonWidthDp()}, ${state.getButtonHeightDp()}) at (${offsetX}, ${offsetY})")
    //var changed by remember { mutableStateOf(0) }
    //var changed by remember { mutableStateOf(false) }
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
                state.getButtonWidthDp(),
                state.getButtonHeightDp()
            )
            //.align(Alignment.Center) // Center the button within the Box
            .clip(RoundedCornerShape(ButtonParameters.buttonRoundedSizes[state.buttonSizeIndex]))//28.dp)) // Apply rounded corners
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(filter) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                        if (filter == null || event.type == filter) {
                            PointerEvents.onButtonPointerEvent(
                                buttonNumber,
                                event,
                                state,
                                stateChanged
                            )
                            if (state.dirty) {
                                stateChanged(state)
                                //changed++
                                //changed = !changed
                            }
                        }
                    }
                }
            }
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = ButtonParameters.buttonTextSizes[state.buttonSizeIndex]
        )
    }
}

@Composable
fun MainBox(
    density: Density,
    state: State,
    filter: PointerEventType? = null,
    stateChanged: (State) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .onGloballyPositioned { coordinates ->
                state.box = Dimensions(
                    Extent.pxToExtent(density, coordinates.size.width.toFloat()),
                    Extent.pxToExtent(density, coordinates.size.height.toFloat()),
                )
            }
            .pointerInput(filter) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        // handle pointer event
                        if (filter == null || event.type == filter) {
                            PointerEvents.onBoxPointerEvent(
                                event,
                                state,
                                stateChanged
                            )
                            if (state.dirty) {
                                stateChanged(state)
                                //changed++
                                //changed = !changed
                            }
                        }
                    }
                }
            }
    ) {
        val buttonWidth =
            with(density) { state.getButtonWidthDp().toPx().roundToInt() }
        val buttonheight =
            with(density) { state.getButtonHeightDp().toPx().roundToInt() }
        val offsetBox1X = state.boxOffset.x.roundToInt()
        val offsetBox1Y = state.boxOffset.y.roundToInt()

        val offsetBox2X = state.boxOffset.x.roundToInt() + buttonWidth + 20
        val offsetBox2Y = state.boxOffset.y.roundToInt()

        val offsetBox3X = state.boxOffset.x.roundToInt()
        val offsetBox3Y = state.boxOffset.y.roundToInt() + buttonheight + 20

        val offsetBox4X = state.boxOffset.x.roundToInt() + buttonheight + 20
        val offsetBox4Y = state.boxOffset.y.roundToInt() + buttonheight + 20

        ButtonBox(
            1, state, filter, "click me 1",
            offsetBox1X,
            offsetBox1Y,
            stateChanged
        )
        ButtonBox(
            2, state, filter, "click me 2",
            offsetBox2X,
            offsetBox2Y,
            stateChanged
        )
        ButtonBox(
            3, state, filter, "click me 3",
            offsetBox3X,
            offsetBox3Y,
            stateChanged
        )
        ButtonBox(
            4, state, filter, "click me 4",
            offsetBox4X,
            offsetBox4Y,
            stateChanged
        )
    }
}
