package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmButtonTapDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    stateChanged: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Called when the user dismisses the dialog (e.g., taps outside)
        title = { Text(text = "Confirmation", color = MaterialTheme.colorScheme.onPrimary) },
        text = { Text(text = "Ok to run command", color = MaterialTheme.colorScheme.onPrimary) },
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
    minimizeButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            minimizeButton()
            stateChanged()
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
    incrementButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            incrementButton()
            stateChanged()
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
    decrementButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            decrementButton()
            stateChanged()
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
    expandButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            expandButton()
            stateChanged()
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
    compressButton: () -> Unit,
    stateChanged: () -> Unit
) {
    Button(
        onClick = {
            compressButton()
            stateChanged()
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
