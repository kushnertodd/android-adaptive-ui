package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

enum class PointerEventState {
    START,
    BOX_PRESS,
    BOX_RELEASE,
    BOX_TAP,
    BUTTON_PRESS,
    BUTTON_BOX_PRESS,
    BUTTON_BOX_RELEASE,
    BUTTON_RELEASE,
    BUTTON_TAP
}

@Composable
fun LogPointerEvents(
    buttonWidth: Dp, buttonHeight: Dp, buttonPadding: Dp, filter: PointerEventType? = null
) {
    var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
    //var buttonPadding by remember { mutableStateOf(8.dp) }
    var buttonPadding by remember { mutableStateOf(0.dp) }
    var previousPosition by remember { mutableStateOf(Offset.Zero) } // Store previous position
    var showDialog by remember { mutableStateOf(false) }
    var ignoreBoxEvent by remember { mutableStateOf(false) } // TODO: unnecessary
    var buttonMoving by remember { mutableStateOf(false) }

    val context = LocalContext.current // Get the current context
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    val screenHeightPx = with(density) { screenHeightDp.toPx() }
    var buttonBoxWidthDp by remember { mutableStateOf(0.dp) }
    var buttonBoxHeightDp by remember { mutableStateOf(0.dp) }
    var boxWidthDp by remember { mutableStateOf(0.dp) }
    var boxHeightDp by remember { mutableStateOf(0.dp) }
    var buttonBoxWidthPx = with(density) { buttonBoxWidthDp.toPx() }
    var buttonBoxHeightPx = with(density) { buttonBoxHeightDp.toPx() }
    var boxWidthPx = with(density) { boxWidthDp.toPx() }
    var boxHeightPx = with(density) { boxHeightDp.toPx() }

    var buttonSizeIndex by remember { mutableStateOf(0) }
    var buttonWidthDp by remember { mutableStateOf(ButtonParameters.buttonWidths[buttonSizeIndex]) }
    var buttonHeightDp by remember { mutableStateOf(ButtonParameters.buttonHeights[buttonSizeIndex]) }

    var buttonWidthPx by remember { mutableStateOf(0f) }
    var buttonHeightPx by remember { mutableStateOf(0f) }

    var offsetX by remember { mutableStateOf(screenWidthPx / 2 + buttonBoxWidthPx / 2) }
    var offsetY by remember { mutableStateOf(screenHeightPx / 2 + buttonBoxHeightPx / 2 / 2) }

    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    // TODO: unnecessary
    val setIgnoreBoxEvent: (Boolean) -> Unit = { newIgnoreBoxEvent ->
        ignoreBoxEvent = newIgnoreBoxEvent
    }
    // TODO: unnecessary
    val testIgnoreBoxEvent: () -> Boolean =
        { ignoreBoxEvent }
    val setButtonMoving: (Boolean) -> Unit = { newButtonMoving ->
        buttonMoving = newButtonMoving
    }
    val testButtonMoving: () -> Boolean =
        { buttonMoving }
    val setFirstPosition: (Offset) -> Unit =
        { startOffset ->
            previousPosition = startOffset
        }
    val setChangePosition: (PointerInputChange) -> Unit =
        { change ->
            // Calculate the difference from the previous position
            val deltaX = change.position.x - previousPosition.x
            val deltaY = change.position.y - previousPosition.y

            // Update offsetX and offsetY based on the drag amount (or delta from previous)
            offsetX += deltaX
            offsetX = max(offsetX, 0f)//-boxWidthDp.value+buttonBoxWidthDp.value/2+40)
            boxWidthPx = with(density) { boxWidthDp.toPx() }
            buttonWidthPx = with(density) { buttonWidthDp.toPx() }
            offsetX = min(
                offsetX,
                boxWidthPx - buttonWidthPx
            )
            boxHeightPx = with(density) { boxHeightDp.toPx() }
            buttonHeightPx = with(density) { buttonHeightDp.toPx() }
            offsetY += deltaY
            offsetY = max(offsetY, 0f)
            offsetY = min(
                offsetY,
                boxHeightPx - buttonHeightPx
            )

            // Update previous position for the next onDrag call
            previousPosition = change.position
            change.consume()
        }
    val setFinalPosition: () -> Unit =
        {
            previousPosition = Offset.Zero
        }
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            buttonWidthDp = ButtonParameters.buttonWidths[buttonSizeIndex]
            buttonHeightDp = ButtonParameters.buttonHeights[buttonSizeIndex]
            buttonWidthPx = with(density) { buttonWidthDp.toPx() }
            buttonHeightPx = with(density) { buttonHeightDp.toPx() }
            offsetX = min(
                offsetX,
                boxWidthPx - buttonWidthPx
            )
            offsetY = min(
                offsetY,
                boxHeightPx - buttonHeightPx
            )
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState -> pointerEventState = newPointerEventState }

    val decrementButtonSize: () -> Unit = {
        if (buttonSizeIndex > 1)
            setButtonSizeIndex(buttonSizeIndex - 2)
    }

    val incrementButtonSize: () -> Unit = {
        if (buttonSizeIndex < ButtonParameters.buttonSizeIndexMax - 1)
            setButtonSizeIndex(buttonSizeIndex + 2)
    }
    val incrementButton: () -> Unit = {
        incrementButtonSize()
        setIgnoreBoxEvent(true) // TODO: unnecessary
    }
    val decrementButton: () -> Unit = {
        decrementButtonSize()
        setIgnoreBoxEvent(true) // TODO: unnecessary
    }

    fun maximizeButton() {
        setButtonSizeIndex(ButtonParameters.buttonSizeIndexMax)
        setIgnoreBoxEvent(true) // TODO: unnecessary
    }

    fun minimizeButton() {
        setButtonSizeIndex(0)
        setIgnoreBoxEvent(true) // TODO: unnecessary
    }

    val onConfirm = {
        decrementButton()
        setIgnoreBoxEvent(true) // TODO: unnecessary
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }
    val onDismiss = {
        incrementButton()
        setIgnoreBoxEvent(true) // TODO: unnecessary
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }

    val setShowDialog: () -> Unit =
        { showDialog = true }

    Column(
        //modifier = Modifier.fillMaxWidth(), // Makes the Column take the full width
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
/*
        Text("box size ${formatDecimals(boxWidthPx,1)} x ${formatDecimals(boxHeightPx,1)}", fontSize = 18.sp)
        Text("button box size ${formatDecimals(buttonBoxWidthPx,1)} x ${formatDecimals(buttonBoxHeightPx,1)}", fontSize = 18.sp)
        Text("offsetX ${formatDecimals(offsetX, 1)} offsetY ${formatDecimals(offsetY,1)}", fontSize = 18.sp)
 */
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 48.sp)
        Text("screen size ${screenWidthPx} x ${screenHeightPx}", fontSize = 18.sp)
        Text("box size ${boxWidthPx} x ${boxHeightPx}", fontSize = 18.sp)
        Text("button box size ${buttonBoxWidthPx} x ${buttonBoxHeightPx}", fontSize = 18.sp)
        Text(
            "offsetX ${formatDecimals(offsetX, 1)} offsetY ${formatDecimals(offsetY, 1)}",
            fontSize = 18.sp
        )
        if (!showDialog) {
            Column() {

                Box(
                    //contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        //.width(250.dp)
                        .height(600.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .onGloballyPositioned { coordinates ->
                            // Convert pixels to DP using LocalDensity
                            boxWidthDp = with(density) { coordinates.size.width.toDp() }
                            boxHeightDp = with(density) { coordinates.size.height.toDp() }
                            boxWidthPx = with(density) { boxWidthDp.toPx() }
                            boxHeightPx = with(density) { boxHeightDp.toPx() }
                        }
                        .pointerInput(filter) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    // handle pointer event
                                    if (filter == null || event.type == filter) {
                                        PointerEvents.onBoxPointerEvent(
                                            event,
                                            pointerEventState,
                                            buttonSizeIndex,
                                            setPointerEventState,
                                            decrementButtonSize,
                                            incrementButtonSize,
                                            setShowDialog,
                                            setFirstPosition,
                                            setChangePosition,
                                            setFinalPosition,
                                            setIgnoreBoxEvent, // TODO: unnecessary
                                            testIgnoreBoxEvent, // TODO: unnecessary
                                            setButtonMoving,
                                            testButtonMoving
                                        )
                                    }
                                }
                            }
                        }
                ) {
                    // The Button composable placed inside the Box
                    Box(
                        //contentAlignment = Alignment.Center,

                        //onClick = {
                        /*
                                                val url = "https://www.google.com"
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    url.toUri()
                                                ) // Create an implicit intent to view a URI
                                                context.startActivity(intent) // Start the activity to handle the intent
                        */
                        //},
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            // Set a specific size for the button
                            //.padding(buttonPadding) // Add some padding around the button
                            .size(
                                ButtonParameters.buttonWidths[buttonSizeIndex],
                                ButtonParameters.buttonHeights[buttonSizeIndex]
                            )
                            //.align(Alignment.Center) // Center the button within the Box
                            .clip(RoundedCornerShape(28.dp)) // Apply rounded corners
                            .background(MaterialTheme.colorScheme.primary)
                            .onGloballyPositioned { coordinates ->
                                // Convert pixels to DP using LocalDensity
                                buttonBoxWidthDp = with(density) { coordinates.size.width.toDp() }
                                buttonBoxHeightDp = with(density) { coordinates.size.height.toDp() }
                                buttonBoxWidthPx = with(density) { buttonBoxWidthDp.toPx() }
                                buttonBoxHeightPx = with(density) { buttonBoxHeightDp.toPx() }
                            }
                            .pointerInput(filter) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        // handle pointer event
                                        if (filter == null || event.type == filter) {
                                            PointerEvents.onButtonPointerEvent(
                                                event,
                                                pointerEventState,
                                                buttonSizeIndex,
                                                setPointerEventState,
                                                setButtonMoving
                                            )
                                        }
                                    }
                                }
                            }
                    ) {
                        Text(
                            text = "Click Me",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = ButtonParameters.buttonTextSizes[buttonSizeIndex]
                        )
                    }
                }
                Row() {
                    Button(
                        onClick = { maximizeButton() }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Sets the background color of the button
                            contentColor = Color.White // Sets the color of the text/content inside the button
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_upward_48dp), // Assuming "my_image.png" was imported
                            contentDescription = "Maximize button"
                        )
                    }
                    Button(
                        onClick = { minimizeButton() }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Sets the background color of the button
                            contentColor = Color.White // Sets the color of the text/content inside the button
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_downward_48dp), // Assuming "my_image.png" was imported
                            contentDescription = "Reset button"
                        )
                    }
                    Button(
                        onClick = { incrementButton() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Sets the background color of the button
                            contentColor = Color.White // Sets the color of the text/content inside the button
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_plus_48dp), // Assuming "my_image.png" was imported
                            contentDescription = "Grow button"
                        )
                    }
                    Button(
                        onClick = { decrementButton() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Sets the background color of the button
                            contentColor = Color.White // Sets the color of the text/content inside the button
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_minus_48dp), // Assuming "my_image.png" was imported
                            contentDescription = "Shrink button"
                        )
                    }
                }
            }
        } else {
            AlertDialog(
                onDismissRequest = onDismiss, // Called when the user dismisses the dialog (e.g., taps outside)
                title = { Text(text = "Confirmation") },
                text = { Text(text = "Ok to run command") },
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


