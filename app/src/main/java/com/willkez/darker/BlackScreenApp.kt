package com.willkez.darker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class BlackScreenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Black Screen Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Keeps overlay service running"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "black_screen_channel"
    }
}
