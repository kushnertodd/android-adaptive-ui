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
    var isUser: Boolean = false
)

/**
 *
 */
object Apps {
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

    /**
     * find app by id
     */
    fun findAppById(id: Int): App? {
        var foundApp: App? = null
        for (app in Apps.allApps) {
            if (id == app.id) {
                foundApp = app
                break // Exit the loop once the first even number is found
            }
        }
        return foundApp
    }
}
