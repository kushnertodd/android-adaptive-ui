package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    //var log by remember { mutableStateOf("") }
    var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
    var buttonPadding by remember { mutableStateOf(16.dp) }
    var buttonSizeIndex by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState -> pointerEventState = newPointerEventState }

    fun decrementButtonSize() {
        if (buttonSizeIndex > 1)
            setButtonSizeIndex(buttonSizeIndex - 2)
        PointerEvents.ignoreBoxEvent = true
    }

    fun incrementButtonSize() {
        if (buttonSizeIndex < ButtonParameters.buttonSizeIndexMax - 1)
            setButtonSizeIndex(buttonSizeIndex + 2)
        PointerEvents.ignoreBoxEvent = true
    }

    fun maximizeButton() {
        setButtonSizeIndex(ButtonParameters.buttonSizeIndexMax)
        PointerEvents.ignoreBoxEvent = true
    }

    fun minimizeButton() {
        setButtonSizeIndex(0)
        PointerEvents.ignoreBoxEvent = true
    }

    val onConfirm = {
        incrementButtonSize()
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }
    val onDismiss = {
        incrementButtonSize()
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }

    val setShowDialog: () -> Unit =
        { showDialog = true }

    Column {
        Text("Adaptive UI", fontSize = 48.sp)
        if (!showDialog) {
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
                                    PointerEvents.onBoxPointerEvent(
                                        event,
                                        pointerEventState,
                                        buttonSizeIndex,
                                        setPointerEventState,
                                        setButtonSizeIndex,
                                        setShowDialog
                                    )
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
                            ButtonParameters.buttonWidths[buttonSizeIndex],
                            ButtonParameters.buttonHeights[buttonSizeIndex]
                        )
                        .align(Alignment.Center) // Center the button within the Box
                        .padding(buttonPadding) // Add some padding around the button
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
                                            setButtonSizeIndex
                                        )
                                    }
                                }
                            }
                        }
                ) {
                    Text(
                        text = "Click Me",
                        fontSize = ButtonParameters.buttonTextSizes[buttonSizeIndex]
                    )
                }
                Column() {
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
                            onClick = { incrementButtonSize() },
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
                    }
                    Row() {
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
                            onClick = { decrementButtonSize() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Sets the background color of the button
                                contentColor = Color.White // Sets the color of the text/content inside the button
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_minus_48dp), // Assuming "my_image.png" was imported
                                contentDescription = "Reset button"
                            )
                        }
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


