package com.facundojaton.applicationa.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        APP_SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )

    companion object {
        const val APP_SHARED_PREFERENCES = "APP_SHARED_PREFERENCES"
        const val NOTIFICATIONS = "NOTIFICATIONS"
    }

    private fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
    /**
     *Save the title and message of the notification if it's not the same as the previous one
     */

    fun saveNotification(newNotification: String) {
        var notifications = getStoredNotifications()
        if (notifications == null) {
            notifications = ArrayList()
        }
        if (notifications.size == 0) {
            notifications.add(newNotification)
        } else if (notifications.last() != newNotification) notifications.add(newNotification)
        val gson = Gson()
        val arrayGSON = gson.toJson(notifications)
        saveString(NOTIFICATIONS, arrayGSON)
    }

    /**
     * @return an ArrayList<String> of the stored notifications on App Shared preferences
     */
    fun getStoredNotifications(): ArrayList<String>? {
        val g = Gson()
        val type = object : TypeToken<List<String>?>() {}.type
        val notificationsJSON = getString(NOTIFICATIONS)
        notificationsJSON?.let {
            return g.fromJson(it, type)
        }
        return ArrayList()
    }
    /**
     * Sets an empty ArrayList<String> on App Shared preferences
     */
    fun clearStoredNotifications() {
        val notifications = ArrayList<String>()
        val gson = Gson()
        val arrayGSON = gson.toJson(notifications)
        saveString(NOTIFICATIONS, arrayGSON)
    }
}