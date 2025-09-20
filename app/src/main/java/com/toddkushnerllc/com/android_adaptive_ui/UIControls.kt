package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun ConfirmButtonTapDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    stateChanged: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Called when the user dismisses the dialog (e.g., taps outside)
        title = { Text(text = "Confirmation") },
        text = { Text(text = "Ok to run command") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                stateChanged()
            }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick =
                    {
                        onDismiss()
                        stateChanged()
                    }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MaximizeButton(
    maximizeButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            maximizeButton()
            stateChanged()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_upward_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Maximize button"
        )
    }
}

@Composable
fun MinimizeButton(
    minimizeButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            minimizeButton()
            stateChanged()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_downward_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Reset button"
        )
    }
}

@Composable
fun IncrementButton(
    incrementButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            incrementButton()
            stateChanged()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_plus_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Grow button"
        )
    }
}

@Composable
fun DecrementButton(
    decrementButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            decrementButton()
            stateChanged()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_minus_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Shrink button"
        )
    }
}

@Composable
fun ExpandButton(
    expandButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            expandButton()
            stateChanged()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_expand_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Shrink button"
        )
    }
}

@Composable
fun CompressButton(
    compressButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            compressButton()
            stateChanged()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_compress_24dp),//48dp), // Assuming "my_image.png" was imported
            contentDescription = "Shrink button"
        )
    }
}
