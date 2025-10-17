package com.toddkushnerllc.android_adaptive_ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.Apps.Companion.createByComponent
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
import kotlin.math.min

fun isUserInstalledApp(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
}

@Composable
fun LogPointerEvents() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
    var buttonSizeIndex by remember { mutableIntStateOf(0) }
    var boxOffset by remember { mutableStateOf(BoxOffset()) }
    var box by remember { mutableStateOf(Dimensions(Extent(), Extent())) }
    var buttonId by remember { mutableIntStateOf(0) }
    // necessary to get ButtonBox pointerInput to reinitialize
    var counter by remember { mutableIntStateOf(0) }

    val getButtonSizeIndex: () -> Int = { buttonSizeIndex }
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            val buttonWidthPx = ButtonParameters.buttonWidthsPx[buttonSizeIndex]
            val buttonHeightPx = ButtonParameters.buttonHeightsPx[buttonSizeIndex]
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - buttonWidthPx
            )
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - buttonHeightPx
            )
            // necessary to get ButtonBox pointerInput to reinitialize
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
    // necessary to get ButtonBox pointerInput to reinitialize
    val getCounter: () -> Int = { counter }
    val setCounter: (Int) -> Unit =
        { newCounter ->
            counter = newCounter
        }
    val context = LocalContext.current // Get the current context
    // playing around with packageManager, getting all installed apps
    val packageManager = context.packageManager
    // my app
    // val myPackageInfo = packageManager.getPackageInfo(context.packageName, 0)
    // val appName = myPackageInfo.applicationInfo.loadLabel(packageManager).toString()
    // want installed packages, not applications
    val installedPackages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(0)
    } else {
        @Suppress("DEPRECATION")
        packageManager.getInstalledPackages(0)
    }
    val systemApps = mutableListOf<String>()
    val userApps = mutableListOf<String>()
    var apps = Apps()
    var id = 0
    for (installedPackage in installedPackages) {
        val packageName = installedPackage.packageName
        if (isUserInstalledApp(context, packageName)) {
            userApps += packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val packageLabel = packageManager.getApplicationLabel(applicationInfo)
            val intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                val componentName = intent.component
                apps.addApp(
                    createByComponent(
                        id++,
                        packageLabel.toString(),
                        packageName,
                        componentName.toString()
                    )
                )
            } else
                log("invalid intent for $packageName")
        } else
            systemApps += packageName
    }
    /*
            log("system apps:")
            for (systemApp in systemApps)
                log("    $systemApp")
            log("user apps:")
            for (userApp in userApps)
                log("     $userApp")
    */
    // getting component name -- not correct
    val packageName = "com.google.ar.lens"
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val packageInfoLabel = packageManager.getApplicationLabel(applicationInfo)
        val componentName = packageInfo.applicationInfo.className
        val isUser = isUserInstalledApp(context, packageName)
        val intent = packageManager.getLaunchIntentForPackage(packageName);
        /*
                if (intent?.resolveActivity(packageManager) != null) {
                    context.startActivity(intent)
                } else
                    log("Invalid application intent: $intent")
        */
    } catch (e: ActivityNotFoundException) {
        log("app $packageName could not be launched: $e")
    } catch (e: PackageManager.NameNotFoundException) {
        log("Google Calculator app not found: ${e.message}")
    }

    val launchDeskClock: (Int, State) -> Unit =
        { launchButtonId, state ->
            val buttonSizeIndex = state.getButtonSizeIndex() // for debugging
            val app = state.apps.findAppById(launchButtonId)
            if (app == null) {
                state.decrementButtonSize()
            } else {
                app.openCount++
                state.setCounter(state.getCounter() + 1)
                try {
                    if (app.intent.resolveActivity(packageManager) != null) {
                        context.startActivity(app.intent)
                    } else
                        log("Invalid application intent: $app.intent")
                } catch (e: ActivityNotFoundException) {
                    log("No app for button id $launchButtonId found: $e")
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
        val buttonSizeIndex = state.getButtonSizeIndex() // for debugging
        // necessary to  recomposes the screen on launching app
        state = newState.copy(noClicks = newState.noClicks + 1)
    }

    fun formatDecimals(number: Float, decimals: Int) =
        String.format("%.${decimals}f", number)
    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 36.sp)
        Spacer(modifier = Modifier.height(30.dp))
        /*
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
        */
        if (!state.showDialog) {
            Column {
                log("reconstituting column")
                val buttonWidthDp = ButtonParameters.buttonWidthsDp[getButtonSizeIndex()]
                val buttonHeightDp = ButtonParameters.buttonHeightsDp[getButtonSizeIndex()]
                log("box button index $buttonSizeIndex gap index ${state.buttonGapPctIndex} (${buttonWidthDp}, ${buttonHeightDp}) ")
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    MaximizeButton(state)
                    Spacer(modifier = Modifier.width(45.dp))
                    MinimizeButton(state)
                    Spacer(modifier = Modifier.width(45.dp))
                    IncrementButton(state)
                    Spacer(modifier = Modifier.width(45.dp))
                    DecrementButton(state)
                    //ExpandButton(state, stateChanged)
                    //CompressButton(state, stateChanged)
                }
                Spacer(modifier = Modifier.height(20.dp))
                MainBox(
                    density,
                    state,
                    stateChanged
                )
            }
        } else {
            ConfirmButtonTapDialog(state)
        }
    }
}


