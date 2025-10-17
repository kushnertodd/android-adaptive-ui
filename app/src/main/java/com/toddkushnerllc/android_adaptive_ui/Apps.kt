package com.toddkushnerllc.android_adaptive_ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
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
    var openCount: Int = priority
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
            val app = App(id, label, priority)
            app.intent.component = ComponentName(packageName, componentName)
            return app
        }

        fun createByAction(
            id: Int, label: String,
            action: String,
            priority: Int = 1
        )
                : App {
            val app = App(id, label, priority)
            app.intent.action = action
            return app
        }

        fun createByContent(
            id: Int,
            label: String,
            action: String,
            content: String,
            priority: Int = 1
        ): App {
            //   startActivityForResult(intent, REQUEST_SELECT_CONTACT)
            val app = App(id, label, priority)
            app.intent.action = action
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
            val app = App(id, label, priority)
            app.intent.action = action
            app.intent.addCategory(category)
            return app
        }

        fun allPhoneApps(context: Context): List<App> {
            val packageManager = context.packageManager
            val installedPackages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getInstalledPackages(0)
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstalledPackages(0)
            }
            var id = 0
            var allApps: MutableList<App> = mutableListOf()
            for (installedPackage in installedPackages) {
                val packageName = installedPackage.packageName
                if (isUserInstalledApp(context, packageName)) {
                    val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                    val packageLabel = packageManager.getApplicationLabel(applicationInfo)
                    val intent = packageManager.getLaunchIntentForPackage(packageName);
                    if (intent != null) {
                        val componentName = intent.component
                        allApps.add(
                            createByComponent(
                                id++,
                                packageLabel.toString(),
                                packageName,
                                componentName.toString()
                            )
                        )
                    } else
                        log("invalid intent for $packageName")
                }
            }
            return allApps
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
            createByComponent(
                id++,
                "Chome",
                "com.android.chrome",
                "com.google.android.apps.chrome.IntentDispatcher",
                3
            )
        )
        addApp(
            createByComponent(
                id++,
                "Maps",
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity",
                2
            )
        )
        addApp(
            createByComponent(
                id++,
                "Calculator",
                "com.google.android.calculator",
                "com.android.calculator2.Calculator",
                2
            )
        )
        addApp(
            createByComponent(
                id++,
                "Calendar",
                "com.google.android.calendar",
                "com.android.calendar.AllInOneActivity",
                3
            )
        )
        addApp(
            createByComponent(
                id++,
                "Camera",
                "com.google.android.GoogleCamera",
                "com.android.camera.CameraLauncher",
                2
            )
        )
        addApp(
            createByComponent(
                id++,
                "Clock",
                "com.google.android.deskclock",
                "com.android.deskclock.DeskClock",
                2
            )
        )
        addApp(
            createByComponent(
                id++,
                "Phone",
                "com.google.android.dialer",
                "com.android.dialer.main.impl.MainActivity",
                3
            )
        )
        addApp(
            createByComponent(
                id++,
                "Docs",
                "com.google.android.apps.docs.editors.docs",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            createByComponent(
                id++,
                "Podcasts",
                "com.podcast.podcasts",
                "fm.castbox.ui.main.MainActivity"
            )
        )
        addApp(
            createByComponent(
                id++,
                "Sheets",
                "com.google.android.apps.docs.editors.sheets",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            createByComponent(
                id++,
                "Slides",
                "com.google.android.apps.docs.editors.slides",
                "com.google.android.apps.docs.app.NewMainProxyActivity"
            )
        )
        addApp(
            createByComponent(
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
                    createByComponent(
                        id++,
                        "News",
                        "com.google.android.apps.magazines",
                        "com.google.apps.dots.android.app.activity.CurrentsStartActivity"
                    )
                )
                addApp(
                    createByComponent(
                        id++,
                        "Contacts",
                        "com.google.android.contacts",
                        "com.android.contacts.activities.PeopleActivity",
                        3
                    )
                )
                addApp(
                    createByComponent(
                        id++,
                        "Wallet",
                        "com.google.android.apps.walletnfcrel",
                        "com.google.commerce.tapandpay.android.wallet.WalletActivity",
                        2
                    )
                )
         arrayOf("kushnertodd@gmail.com"),
        "from adaptive UI",
        addresses:Array<String>, subject:String,
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri() // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
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