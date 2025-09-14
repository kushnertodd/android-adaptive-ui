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
    onDismiss: () -> Unit
) {
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

@Composable
fun MaximizeButton(
    maximizeButton: () -> Unit
) {
    Button(
        onClick = { maximizeButton() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Sets the background color of the button
            contentColor = Color.White // Sets the color of the text/content inside the button
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_upward_48dp), // Assuming "my_image.png" was imported
            contentDescription = "Maximize button"
        )
    }
}

@Composable
fun MinimizeButton(
    minimizeButton: () -> Unit
) {
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
}

@Composable
fun IncrementButton(
    incrementButton: () -> Unit
) {
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
}

@Composable
fun DecrementButton(
    decrementButton: () -> Unit
) {
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
