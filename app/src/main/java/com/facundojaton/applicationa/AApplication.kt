package com.facundojaton.applicationa

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDexApplication

class AApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
            val id = getString(R.string.notification_channel_general_id)
            val name = getString(R.string.notification_channel_general_name)
            val descriptionChannel = getString(R.string.notification_channel_general_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionChannel
            }
            ContextCompat.getSystemService(
                this, NotificationManager::class.java
            )?.createNotificationChannel(channel)
    }

}