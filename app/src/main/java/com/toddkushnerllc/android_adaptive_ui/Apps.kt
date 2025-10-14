package com.toddkushnerllc.android_adaptive_ui

import java.time.Instant
import kotlin.math.ln

/** instance of opening app  */
data class AppOpen(
    /** wall-clock milliseconds app opened */
    val openTime: Long = Instant.now().toEpochMilli(),
    /** number of app opens since launcher installed */
    val openCount: Int = 0,
    /** average tap pressure */
    val pressure: Float = 0.0f,
    /** duration of open tap in milliseconds */
    val duration: Int = 0,
) {
    val millisecondsSinceEpoch: Long
        get() = Instant.now().toEpochMilli() - openTime

    /**
     * weight of app open to determine value of app
     * serving selection for formula based on milliseconds since open is:
     * the exact formulation is a secret sauce
     * - 0 sec = 1.0
     * - longer = 1/ln(secs+10)
     * seconds 0       20      40      60      80      100     120
     * score   1.00    0.68    0.59    0.54    0.51    0.49    0.47
     * */
    val score: Double
        get() {
            val durationMilliseconds = millisecondsSinceEpoch
            val durationSeconds = durationMilliseconds / (1000.0)
            return 1 / ln(durationSeconds + 10)
        }
}

data class App(
    /** id (= button id) */
    var id: Int = 0,
    /** display name */
    var label: String = "",
    /** android package */
    var packageName: String = "",
    /** android component */
    var componentName: String = "",
    /** user installed app */
    var isUser: Boolean = false,
    /** open history for app */
    var appOpens: List<AppOpen> = listOf()
) : Comparable<App> {
    override fun compareTo(other: App): Int {
        // Sort by age by default
        return label.compareTo(other.label)
    }
}

/**
 *
 */
class Apps {
    /**
     * find app by id
     */
    fun findAppById(id: Int): App? {
        var foundApp: App? = null
        for (app in allApps) {
            if (id == app.id) {
                foundApp = app
                break // Exit the loop once the first even number is found
            }
        }
        return foundApp
    }

    /** time launcher installed  */
    var epoch = Instant.now().toEpochMilli()

    /** all installed apps tracked by launcher */
    var allApps: MutableList<App> = mutableListOf<App>()

    /** time series of all app opens */
    var appOpens: MutableList<AppOpen> = mutableListOf<AppOpen>()

    /** count of all app opens since launcher installed */
    val appCount: Int
        get() = allApps.size

    /** count of all app opens since launcher installed */
    val appOpensCount: Int
        get() = appOpens.size

    init {
        addApp(
            App(
                0,
                "Chrome",
                "com.android.chrome",
                "com.google.android.apps.chrome.IntentDispatcher",
                true
            )
        )
        addApp(
            App(
                1,
                "Maps",
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity",
                true
            )
        )
        addApp(
            App(
                2,
                "Calculator",
                "com.google.android.calculator",
                "com.android.calculator2.Calculator",
                true
            )
        )
        addApp(
            App(
                3,
                "Calendar",
                "com.google.android.calendar",
                "com.android.calendar.AllInOneActivity",
                true
            )
        )
        addApp(
            App(
                4,
                "Camera",
                "com.google.android.GoogleCamera",
                "com.android.camera.CameraLauncher",
                true
            )
        )
        addApp(
            App(
                5,
                "Clock",
                "com.google.android.deskclock",
                "com.android.deskclock.DeskClock",
                true
            )
        )
        addApp(
            App(
                6,
                "Phone",
                "com.google.android.dialer",
                "com.android.dialer.main.impl.MainActivity",
                true
            )
        )
        addApp(
            App(
                7,
                "Docs",
                "com.google.android.apps.docs.editors.docs",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        addApp(
            App(
                8,
                "Podcasts",
                "com.podcast.podcasts",
                "fm.castbox.ui.main.MainActivity",
                true
            )
        )
        addApp(
            App(
                9,
                "Sheets",
                "com.google.android.apps.docs.editors.sheets",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        addApp(
            App(
                10,
                "Slides",
                "com.google.android.apps.docs.editors.slides",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        addApp(
            App(
                11,
                "Lens",
                "com.google.ar.lens",
                "com.google.vr.apps.ornament.app.lens.LensLauncherActivity",
                true
            )
        )
        /*
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
        */

    }

    /**
     * add launcher tracked app
     */
    fun addApp(app: App) {
        allApps += app
    }

    /**
     * remove launcher tracked app
     */
    fun removeApp(app: App): Boolean {
        return allApps.remove(app)
    }

    /**
     * add launcher tracked app open
     */
    fun addAppOpen(appOpen: AppOpen) {
        appOpens += appOpen
    }

    /**
     * remove launcher tracked app open
     */
    fun removeAppOpen(appOpen: AppOpen): Boolean {
        return appOpens.remove(appOpen)
    }


}
