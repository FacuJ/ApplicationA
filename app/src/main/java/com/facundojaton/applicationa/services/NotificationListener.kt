package com.facundojaton.applicationa.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.facundojaton.applicationa.utils.AppSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    object InterceptedNotificationCode {
        const val OTHER_NOTIFICATIONS_CODE = 4 // We ignore all notification with code == 4
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getCharSequence("android.text").toString()
        val preferences = AppSharedPreferences(applicationContext)
        preferences.saveNotification(title)
        val getArrayTest = preferences.getStoredNotifications()
        getArrayTest?.let {
            for (element in it) {
                Log.e(NotificationListener::class.java.simpleName, element)
            }
        }
        /*val notificationCode = matchNotificationCode(sbn)
        if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            val intent = Intent("com.facundojaton.applicationa")
            intent.putExtra("Notification Code", notificationCode)
            sendBroadcast(intent)
        }*/
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val notificationCode = matchNotificationCode(sbn)
        /*if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            val activeNotifications = this.activeNotifications
            if (activeNotifications != null && activeNotifications.size > 0) {
                for (i in activeNotifications.indices) {
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        val intent = Intent("com.facundojaton.applicationa")
                        intent.putExtra("Notification Code", notificationCode)
                        sendBroadcast(intent)
                        break
                    }
                }
            }
        }*/
    }

    private fun matchNotificationCode(sbn: StatusBarNotification): Int {
        //val packageName = sbn.packageName
        return InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE
    }
}