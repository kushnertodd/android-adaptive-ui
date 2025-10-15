package com.toddkushnerllc.android_adaptive_ui

import android.content.ComponentName
import android.content.Intent
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
    /** default frequency of use */
    var priority: Int = 1,
    /** user installed app */
    var isUser: Boolean = false,
) {
    //:Comparable<App> {
    /** open history for app */
    var appOpens: List<AppOpen> = listOf()
    var opensCount: Int = priority
    var intent = Intent()

    /*
        override fun compareTo(other: App): Int {
            return when {
                opens == other.opens ->
                    label.compareTo(other.label)

                else -> opens.compareTo(other.opens)
            }
        return label.compareTo(other.label)
    }
    */
    fun setComponent(packageName: String, componentName: String) {
        intent.component = ComponentName(packageName, componentName)
    }
}

/**
 *
 */
class Apps {
    companion object {
        fun createByComponent(
            id: Int,
            label: String,
            packageName: String,
            componentName: String,
            priority: Int = 1
        ): App {
            var app = App(id, label, priority)
            app.intent.setComponent(ComponentName(packageName, componentName))
            return app
        }

        fun createByAction(
            id: Int, label: String,
            action: String,
            priority: Int = 1
        )
                : App {
            var app = App(id, label, priority)
            app.intent.setAction(action)
            return app
        }

        fun createByContent(
            id: Int,
            label: String,
            action: String,
            content: String,
            priority: Int = 1
        )
                : App {
            //   startActivityForResult(intent, REQUEST_SELECT_CONTACT)
            var app = App(id, label, priority)
            app.intent.setAction(action)
            app.intent.type = content
            return app
        }

        fun createByCategory(
            id: Int,
            label: String,
            action: String,
            category: String,
            priority: Int = 1
        )
                : App {
            var app = App(id, label, priority)
            app.intent.setAction(action)
            app.intent.addCategory(category)
            return app
        }
    }

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
    var allApps: MutableList<App> = mutableListOf()

    /** time series of all app opens */
    var appOpens: MutableList<AppOpen> = mutableListOf()

    /** count of all app opens since launcher installed */
    val appCount: Int
        get() = allApps.size

    /** count of all app opens since launcher installed */
    val appOpensCount: Int
        get() = appOpens.size

    init {
        var id = 0
        addApp(
            Apps.createByComponent(
                id++,
                "Chome",
                "com.android.chrome",
                "com.google.android.apps.chrome.IntentDispatcher",
                3
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Maps",
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity",
                2
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Calculator",
                "com.google.android.calculator",
                "com.android.calculator2.Calculator",
                2
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Calendar",
                "com.google.android.calendar",
                "com.android.calendar.AllInOneActivity",
                3
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Camera",
                "com.google.android.GoogleCamera",
                "com.android.camera.CameraLauncher",
                2
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Clock",
                "com.google.android.deskclock",
                "com.android.deskclock.DeskClock",
                2
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Phone",
                "com.google.android.dialer",
                "com.android.dialer.main.impl.MainActivity",
                3
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Docs",
                "com.google.android.apps.docs.editors.docs",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Podcasts",
                "com.podcast.podcasts",
                "fm.castbox.ui.main.MainActivity"
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Sheets",
                "com.google.android.apps.docs.editors.sheets",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Slides",
                "com.google.android.apps.docs.editors.slides",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            Apps.createByComponent(
                id++,
                "Lens",
                "com.google.ar.lens",
                "com.google.vr.apps.ornament.app.lens.LensLauncherActivity",
                2
            )
        )
        /*
                // works, no return
                addApp(
                    Apps.createByCategory(
                        id++,
                        "Email",
                        Intent.ACTION_MAIN,
                        Intent.CATEGORY_APP_EMAIL,
                        3
                    )
                )
                addApp(
                    Apps.createByComponent(
                        id++,
                        "News",
                        "com.google.android.apps.magazines",
                        "com.google.apps.dots.android.app.activity.CurrentsStartActivity"
                    )
                )
                addApp(
                    Apps.createByComponent(
                        id++,
                        "Contacts",
                        "com.google.android.contacts",
                        "com.android.contacts.activities.PeopleActivity",
                        3
                    )
                )
                addApp(
                    Apps.createByComponent(
                        id++,
                        "Wallet",
                        "com.google.android.apps.walletnfcrel",
                        "com.google.commerce.tapandpay.android.wallet.WalletActivity",
                        2
                    )
                )
        */

        //                    12 -> component = ComponentName(
        //                        "com.google.android.apps.magazines",
        //                        "com.google.apps.dots.android.app.activity.CurrentsStartActivity"
        //                    )
        //                    13 -> component = ComponentName(
        //                        "com.google.android.contacts",
        //                        "com.android.contacts.activities.PeopleActivity"
        //                    )
        //                    15 -> component = ComponentName(
        //                        "com.google.android.apps.walletnfcrel",
        //                        "com.google.commerce.tapandpay.android.wallet.WalletActivity"
        //                    )

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
