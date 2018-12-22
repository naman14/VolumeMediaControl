package com.naman14.volumemedia

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.Context
import android.media.AudioManager
import android.preference.PreferenceManager

class MediaVolumeService : AccessibilityService() {

    var needForce = false

    companion object {
        var enabledPackageList: List<String>? = null
        val enabledPackages: String = "enabled_packages"

        val defaultApps = hashMapOf<String, String>().apply {
            this["Netflix"] = "com.netflix.mediaclient"
            this["Twitch"] = "tv.twitch.android.app"
            this["Youtube"] = "com.google.android.youtube"
            this["Youtube Gaming"] = "com.google.android.apps.youtube.gaming"
            this["Prime Video"] = "com.amazon.avod.thirdpartyclient"
            this["Google Play Music"] = "com.google.android.music"
        }

        fun getEnabledPackageList(context: Context): List<String> {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val set = prefs.getStringSet(enabledPackages, defaultApps.values.toSet())
            return set.toList()
        }

        fun setEnabledPackageList(context: Context, set: Set<String>) {
            this.enabledPackageList = set.toList()
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().putStringSet(MediaVolumeService.enabledPackages, set).apply()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (enabledPackageList == null) {
            enabledPackageList = getEnabledPackageList(this)
        }

        if (event.packageName != "com.android.systemui") {
            needForce = enabledPackageList!!.contains(event.packageName)
        } else {
            if (needForce) {
                Utils.forceVolumeControlStream(this, AudioManager.STREAM_MUSIC)
            } else {
                Utils.forceVolumeControlStream(this, -1)
            }
        }
    }


    override fun onInterrupt() {
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

//        val info = AccessibilityServiceInfo()
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
//        info.description = getString(R.string.description)
//        serviceInfo = info

        enabledPackageList = getEnabledPackageList(this)
    }
}