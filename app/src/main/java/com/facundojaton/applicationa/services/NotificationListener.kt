package com.facundojaton.applicationa.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.facundojaton.applicationa.utils.AppSharedPreferences

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getCharSequence("android.title")
        val message = sbn.notification.extras.getCharSequence("android.text")
        if (!title.isNullOrBlank() || !message.isNullOrBlank()) {
            val dataToStore = "${title.toString()} : ${message.toString()}"
            val preferences = AppSharedPreferences(applicationContext)
            preferences.saveNotification(dataToStore)
        }
    }
}