package com.facundojaton.applicationa.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.facundojaton.applicationa.utils.AppSharedPreferences

class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getCharSequence("android.title")
        val message = sbn.notification.extras.getCharSequence("android.text")
        if (!title.isNullOrBlank() || !message.isNullOrBlank()) {
            val dataToStore = "${title.toString()} : ${message.toString()}"
            val preferences = AppSharedPreferences(applicationContext)
            preferences.saveNotification(dataToStore)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //START_STICKY  to order the system to restart your service as soon as possible when it was killed.
        return START_STICKY
    }

    override fun onDestroy() {
        requestUnbind()
        super.onDestroy()
    }
}