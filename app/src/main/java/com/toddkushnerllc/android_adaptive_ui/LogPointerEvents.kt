package com.toddkushnerllc.android_adaptive_ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
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
    var buttonId by remember { mutableStateOf(0) }
    var counter by remember { mutableStateOf(0) }

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
            counter++
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
    val getButtonId: () -> Int = { buttonId }
    val setButtonId: (Int) -> Unit =
        { newButtonId ->
            buttonId = newButtonId
        }
    val getCounter: () -> Int = { counter }
    val setCounter: (Int) -> Unit =
        { newCounter ->
            counter = newCounter
        }
    val context = LocalContext.current // Get the current context
    val packageManager = context.packageManager
    // my app
    val myPackageInfo = packageManager.getPackageInfo(context.packageName, 0)
    val appName = myPackageInfo.applicationInfo.loadLabel(packageManager).toString()
    // installed apps
    /*   val installedApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           packageManager.getInstalledApplications(0)
       } else {
           @Suppress("DEPRECATION")
           packageManager.getInstalledApplications(0)
       }
    */
    val installedPackages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(0)
    } else {
        @Suppress("DEPRECATION")
        packageManager.getInstalledPackages(0)
    }
    val amazonPackage = installedPackages[2]
    log("amazon package:")
    log("  packageName: ${amazonPackage.packageName}")
    log("  name: ${amazonPackage.applicationInfo.name}")
    log("  dataDir: ${amazonPackage.applicationInfo.dataDir}")
    log("  flags: ${amazonPackage.applicationInfo.flags}")
    // does not work
    try {
        val packageName = "com.android.chrome"
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val componentName = packageInfo.applicationInfo.className
        log("info for package ${packageName} component ${componentName}")
        //for (packageInfo in installedPackages) {
        //val packageName = packageInfo.packageName
        //val applicationInfo = packageInfo.applicationInfo

/*
        // Access ComponentInfo for activities (example)
        val activities = packageInfo.activities
        activities?.forEach { activityInfo ->
            // Access properties of activityInfo, e.g., activityInfo.name, activityInfo.labelRes
            log("Activity: ${activityInfo.name}")
        }

        // Access ComponentInfo for services (example)
        val services = packageInfo.services
        services?.forEach { serviceInfo ->
            // Access properties of serviceInfo
            log("Service: ${serviceInfo.name}")
        }
*/
        //}
    } catch (e: PackageManager.NameNotFoundException) {
        log("Google Calculator app not found: ${e.message}")
    }

    val launchDeskClock: (Int, Array<String>, String, State) -> Unit =
        { button_id, addresses, subject, state ->
//        val intent = Intent(Intent.ACTION_SENDTO).apply {
//            data = "mailto:".toUri() // Only email apps handle this.
//            putExtra(Intent.EXTRA_EMAIL, addresses)
//            putExtra(Intent.EXTRA_SUBJECT, subject)
//        }
            val app = Apps.findAppById(button_id)
            if (app == null) {
                state.decrementButtonSize()
            } else {
                val intent =
                    Intent().setComponent(ComponentName(app.packageName, app.componentName))
                /*
                                when (button_id) {
                                    0 -> component = ComponentName(
                                        "com.android.chrome",
                                        "com.google.android.apps.chrome.IntentDispatcher"
                                    )

                                    1 -> component = ComponentName(
                                        "com.google.android.apps.maps",
                                        "com.google.android.maps.MapsActivity"
                                    )

                                    2 -> component = ComponentName(
                                        "com.google.android.calculator",
                                        "com.android.calculator2.Calculator"
                                    )

                                    3 -> component = ComponentName(
                                        "com.google.android.calendar",
                                        "com.android.calendar.AllInOneActivity"
                                    )

                                    4 -> component = ComponentName(
                                        "com.google.android.GoogleCamera",
                                        "com.android.camera.CameraLauncher"
                                    )

                                    5 -> component = ComponentName(
                                        "com.google.android.deskclock",
                                        "com.android.deskclock.DeskClock"
                                    )

                                    6 -> component = ComponentName(
                                        "com.google.android.dialer",
                                        "com.android.dialer.main.impl.MainActivity"
                                    )

                                    7 -> component = ComponentName(
                                        "com.google.android.apps.docs.editors.docs",
                                        "com.google.android.apps.docs.app.NewMainProxyActivity"
                                    )

                                    8 -> component = ComponentName(
                                        "com.podcast.podcasts",
                                        "fm.castbox.ui.main.MainActivity"
                                    )

                                    9 -> component = ComponentName(
                                        "com.google.android.apps.docs.editors.sheets",
                                        "com.google.android.apps.docs.app.NewMainProxyActivity"
                                    )

                                    10 -> component = ComponentName(
                                        "com.google.android.apps.docs.editors.slides",
                                        "com.google.android.apps.docs.app.NewMainProxyActivity"
                                    )

                                    11 -> component = ComponentName(
                                        "com.google.ar.lens",
                                        "com.google.vr.apps.ornament.app.lens.LensLauncherActivity"
                                    )

                // works, no return
                //                    12 -> component = ComponentName(
                //                        "com.google.android.apps.magazines",
                //                        "com.google.apps.dots.android.app.activity.CurrentsStartActivity"
                //                    )
                //                    13 -> component = ComponentName(
                //                        "com.google.android.contacts",
                //                        "com.android.contacts.activities.PeopleActivity"
                //                    )
                //                    14 -> component = ComponentName(
                //                        "com.google.android.contacts",
                //                        "com.android.contacts.activities.PeopleActivity"
                //                    )
                //                    15 -> component = ComponentName(
                //                        "com.google.android.apps.walletnfcrel",
                //                        "com.google.commerce.tapandpay.android.wallet.WalletActivity"
                //                    )
                                    else -> {
                                        //state.decrementButtonSize()
                                        //launch = false
                                    }
                                }
                                //component = ComponentName(packageName, componentName)
                            }
                */
                try {
                    //log("starting ${packageName} component ${componentName}")
                    //if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    //}
                } catch (e: ActivityNotFoundException) {
                    log("No app for button id ${button_id} found: ${e}")
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
                getButtonId,
                setButtonId,
                getCounter,
                setCounter,
                launchDeskClock
            )
        )
    }
    // never called, eventually remove
    val stateChanged: (State) -> Unit = { newState ->
        state.dirty = false
        state = newState.copy()
    }

    fun formatDecimals(number: Float, decimals: Int) =
        String.format("%.${decimals}f", number)
    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 36.sp)
        Text(
            "screen size ${state.screen.width.dp} x ${state.screen.height.dp}",
            fontSize = 12.sp
        )
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
                    density,
                    state,
                    filter,
                    counter,
                    stateChanged
                )
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    MaximizeButton(state, stateChanged)
                    Spacer(modifier = Modifier.width(45.dp))
                    MinimizeButton(state, stateChanged)
                    Spacer(modifier = Modifier.width(45.dp))
                    IncrementButton(state, stateChanged)
                    Spacer(modifier = Modifier.width(45.dp))
                    DecrementButton(state, stateChanged)
                    //ExpandButton(state, stateChanged)
                    //CompressButton(state, stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(state)
        }
       // counter++
    }
}


