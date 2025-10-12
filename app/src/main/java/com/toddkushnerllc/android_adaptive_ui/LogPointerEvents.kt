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
    var buttonId by remember { mutableStateOf(0) }

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
    val getButtonId: () -> Int = { buttonId }
    val setButtonId: (Int) -> Unit =
        { newButtonId ->
            buttonId = newButtonId
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
        val packageName = "com.google.android.calculator"
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val componentName = packageInfo.applicationInfo.className
        log("info for package ${packageName} component ${componentName}:")
        //for (packageInfo in installedPackages) {
        //val packageName = packageInfo.packageName
        //val applicationInfo = packageInfo.applicationInfo

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
            var launch = true
            val intent = Intent().apply {
                when (button_id) {
                    /*            0 -> component = ComponentName(
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
                                4 -> component = ComponentName(
                                    "com.google.android.apps.walletnfcrel",
                                    "com.google.commerce.tapandpay.android.wallet.WalletActivity"
                                )                   //else -> throw IllegalArgumentException("launchDeskClock: invalid button id ${button_id}")
            */
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

                    4 -> component = ComponentName(
                        "bbc.mobile.news.ww",
                        "com.mobile.MainActivity"
                    )

                    5 -> component = ComponentName(
                        "chipolo.net.v3",
                        "net.chipolo.app.ui.main.MainActivity"
                    )

                    6 -> component = ComponentName(
                        "com.abc.abcnews",
                        "com.disney.bootstrap.activity.bootstrap.BootstrapActivity"
                    )

                    7 -> component = ComponentName(
                        "com.amazon.mShop.android.shopping",
                        "com.amazon.mShop.home.HomeActivity"
                    )

                    8 -> component = ComponentName(
                        "com.android.chrome",
                        "com.google.android.apps.chrome.IntentDispatcher"
                    )

                    9 -> component = ComponentName(
                        "com.bose.bosemusic",
                        "com.bose.madrid.SplashScreenActivity"
                    )

                    10 -> component = ComponentName(
                        "com.cbs.app",
                        "com.paramount.android.pplus.features.splash.mobile.integration.SplashActivity"
                    )

                    11 -> component = ComponentName(
                        "com.citi.citimobile",
                        "com.citi.mobile.pt3.GlobalPhoneActivity"
                    )

                    12 -> component = ComponentName(
                        "com.disney.disneyplus",
                        "com.bamtechmedia.dominguez.main.MainActivity"
                    )

                    13 -> component = ComponentName(
                        "com.fitbit.FitbitMobile",
                        "com.fitbit.FirstActivity"
                    )

                    14 -> component = ComponentName(
                        "com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"
                    )

                    15 -> component = ComponentName(
                        "com.google.android.GoogleCamera",
                        "com.android.camera.CameraLauncher"
                    )

                    16 -> component = ComponentName(
                        "com.google.ar.lens",
                        "com.google.vr.apps.ornament.app.lens.LensLauncherActivity"
                    )

                    17 -> component = ComponentName(
                        "com.hulu.plus",
                        "com.hulu.features.splash.SplashActivity"
                    )

                    18 -> component = ComponentName(
                        "com.microsoft.amp.apps.bingnews",
                        "com.microsoft.sapphire.app.main.SapphireMainActivity"
                    )

                    19 -> component = ComponentName(
                        "com.morganstanley.clientmobile.prod",
                        "com.morganstanley.mscordova.shim.MSShimActivity"
                    )

                    20 -> component = ComponentName(
                        "com.omronhealthcare.omronconnect",
                        "com.omronhealthcare.foresight.view.module.splash.SplashActivity"
                    )

                    21 -> component = ComponentName(
                        "com.podcast.podcasts",
                        "fm.castbox.ui.main.MainActivity"
                    )

                    22 -> component = ComponentName(
                        "com.resmed.myair",
                        "com.resmed.mon.presentation.workflow.authentication.launch.LaunchActivity"
                    )

                    23 -> component = ComponentName(
                        "com.rhmsoft.edit.pro",
                        "com.rhmsoft.edit.activity.MainActivity"
                    )

                    24 -> component = ComponentName(
                        "com.sillens.shapeupclub",
                        "com.lifesum.android.main.MainActivity"
                    )

                    25 -> component = ComponentName(
                        "com.socialnmobile.dictapps.notepad.color.note",
                        "com.socialnmobile.colornote.activity.Main"
                    )

                    26 -> component = ComponentName(
                        "com.verizon.messaging.vzmsgs",
                        "com.verizon.mms.ui.activity.Provisioning"
                    )

                    27 -> component = ComponentName(
                        "com.vrbo.android",
                        "com.expedia.bookings.activity.SearchActivity"
                    )

                    28 -> component = ComponentName(
                        "com.vzw.hss.myverizon",
                        "com.vzw.mobilefirst.setup.views.activity.SetUpActivity"
                    )

                    29 -> component = ComponentName(
                        "com.wbd.stream",
                        "com.wbd.fuse.appcore.FuseActivity"
                    )

                    30 -> component = ComponentName(
                        "com.zillow.android.zillowmap",
                        "com.zillow.android.splashscreen.SplashScreenActivity"
                    )

                    31 -> component = ComponentName(
                        "cz.hipercalc.pro",
                        "app.hipercalc.CalculatorActivity"
                    )

                    32 -> component = ComponentName(
                        "gov.dhs.tsa.mytsa",
                        "gov.dhs.mytsa.ui.splash.SplashActivity"
                    )

                    33 -> component = ComponentName(
                        "net.sharewire.parkmobilev2",
                        "net.easypark.android.mvp.splash.SplashActivity"
                    )

                    34 -> component = ComponentName(
                        "nz.co.vista.android.movie.metrotheatres",
                        "com.webedia.optimusprime.ui.splash.SplashActivity"
                    )

                    35 -> component = ComponentName(
                        "org.npr.android.news",
                        "org.npr.one.StartActivity"
                    )

                    36 -> component = ComponentName(
                        "photo.editor.photoeditor.photoeditorpro",
                        "com.camerasideas.collagemaker.activity.DummyActivity"
                    )

                    37 -> component = ComponentName(
                        "tool.audio.cutter.ringtonemaker",
                        "com.ijoysoft.ringtone.activity.WelcomeActivity"
                    )

                    38 -> component = ComponentName(
                        "us.zoom.videomeetings",
                        "com.zipow.videobox.LauncherActivity"
                    )

                    else -> {
                        //state.decrementButtonSize()
                        //launch = false
                    }
                }
                //component = ComponentName(packageName, componentName)
            }
            if (launch) {
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


