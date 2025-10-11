package com.toddkushnerllc.android_adaptive_ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
import kotlin.math.min

@Composable
fun LogPointerEvents(
    filter: PointerEventType? = null
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
    var buttonSizeIndex by remember { mutableStateOf(0) }
    var boxOffset by remember { mutableStateOf(BoxOffset()) }
    var box by remember { mutableStateOf(Dimensions(Extent(), Extent())) }

    val getButtonSizeIndex: () -> Int = { buttonSizeIndex }
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - ButtonParameters.buttonWidthsPx[buttonSizeIndex]
            )
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - ButtonParameters.buttonHeightsPx[buttonSizeIndex]
            )
        }
    val getPointerEventState: () -> PointerEventState = { pointerEventState }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState ->
            pointerEventState = newPointerEventState
        }
    val getBoxOffset: () -> BoxOffset = { boxOffset }
    val setBoxOffset: (BoxOffset) -> Unit =
        { newBoxOffset ->
            boxOffset = newBoxOffset
        }
    val getBox: () -> Dimensions = { box }
    val setBox: (Dimensions) -> Unit =
        { newBox ->
            box = newBox
        }
    val context = LocalContext.current // Get the current context
    val packageManager = context.packageManager
    var launch = true
    val installedApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getInstalledApplications(0)
    }

    val launchDeskClock: (Int, Array<String>, String, State) -> Unit =
        { button_id, addresses, subject, state ->
//        val intent = Intent(Intent.ACTION_SENDTO).apply {
//            data = "mailto:".toUri() // Only email apps handle this.
//            putExtra(Intent.EXTRA_EMAIL, addresses)
//            putExtra(Intent.EXTRA_SUBJECT, subject)
//        }
            /*
            com.google.android.calculator/com.android.calculator2.Calculator
            com.google.android.calendar/com.android.calendar.AllInOneActivity
            com.google.android.contacts/com.android.contacts.activities.PeopleActivity
            com.google.android.deskclock/com.android.deskclock.DeskClock
            com.google.android.dialer/com.android.dialer.main.impl.MainActivity
            */
            val packageName = "com.google.android.calculator"
            //val componentName = "com.google.android.contacts/com.android.contacts.activities.PeopleActivity"
            val componentName = "com.android.calculator2.Calculator"
            val intent = Intent().apply {
                when (button_id) {
                    0 -> component = ComponentName(
                        "com.google.android.calculator",
                        "com.android.calculator2.Calculator"
                    )

                    1 -> component = ComponentName(
                        "com.google.android.calendar",
                        "com.android.calendar.AllInOneActivity"
                    )

                    2 -> component = ComponentName(
                        "com.google.android.deskclock",
                        "com.android.deskclock.DeskClock"
                    )

                    3 -> component = ComponentName(
                        "com.google.android.dialer",
                        "com.android.dialer.main.impl.MainActivity"
                    )
                    //else -> throw IllegalArgumentException("launchDeskClock: invalid button id ${button_id}")
                    else -> {
                        //state.decrementButtonSize()
                        launch = false
                    }
                }
                //component = ComponentName(packageName, componentName)
            }
            if (launch) {
                try {
                    //if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    //}
                } catch (e: ActivityNotFoundException) {
                    log("No app package ${packageName} component ${componentName} found: ${e}")
                }
            }
        }
    var state by remember {
        mutableStateOf(
            State(
                configuration,
                density,
                getButtonSizeIndex,
                setButtonSizeIndex,
                getPointerEventState,
                setPointerEventState,
                getBoxOffset,
                setBoxOffset,
                getBox,
                setBox,
                launchDeskClock
            )
        )
    }
    // never called, eventually remove
    val stateChanged: (State) -> Unit = { newState ->
        state.dirty = false
        state = newState.copy()
    }

    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 36.sp)
        Text("screen size ${state.screen.width.dp} x ${state.screen.height.dp}", fontSize = 12.sp)
        Text(
            "screen size ${state.screen.width.px}.px x ${state.screen.height.px}.px",
            fontSize = 12.sp
        )
        Text(
            "button size ${ButtonParameters.buttonWidthsDp[getButtonSizeIndex()]} x ${ButtonParameters.buttonHeightsDp[getButtonSizeIndex()]}",
            fontSize = 12.sp
        )
        Text(
            "button size ${ButtonParameters.buttonWidthsPx[getButtonSizeIndex()]}.px x ${ButtonParameters.buttonHeightsPx[getButtonSizeIndex()]}.px",
            fontSize = 12.sp
        )
        Text(
            "offsetX ${formatDecimals(boxOffset.x, 1)}.px " +
                    "offsetY ${
                        formatDecimals(boxOffset.y, 1)
                    }.px",
            fontSize = 12.sp
        )
        if (!state.showDialog) {
            Column() {
                log("reconstituting column")
                log("box button index ${buttonSizeIndex} gap index ${state.buttonGapPctIndex} (${ButtonParameters.buttonWidthsDp[getButtonSizeIndex()]}, ${ButtonParameters.buttonHeightsDp[getButtonSizeIndex()]}) ")
                MainBox(
                    density, state, filter,
                    stateChanged
                )
                Row() {
                    MaximizeButton(state, stateChanged)
                    MinimizeButton(state, stateChanged)
                    IncrementButton(state, stateChanged)
                    DecrementButton(state, stateChanged)
                    ExpandButton(state, stateChanged)
                    CompressButton(state, stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(state)
        }
    }
}


