package com.toddkushnerllc.com.android_adaptive_ui

import android.content.Context
import android.content.pm.PackageManager


class AppPackageNameFetcher {
    fun getInstalledAppPackageNames(context: Context) {
        val packageManager = context.getPackageManager()
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (appInfo in installedApps) {
            val packageName = appInfo.packageName
            println("Installed App Package Name: $packageName")
            // You can also get the application label (display name)
            val appLabel = packageManager.getApplicationLabel(appInfo).toString()
            println("Installed App Label: $appLabel")
        }
    }
}