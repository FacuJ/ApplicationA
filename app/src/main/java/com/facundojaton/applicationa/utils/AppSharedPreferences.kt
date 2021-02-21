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

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveBoolean(key: String, value: Boolean?) {
        val editor = sharedPreferences.edit()
        value?.let { editor.putBoolean(key, it) }
        editor.apply()
    }

    fun saveInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun saveNotification(newNotification: String) {
        var notifications = getStoredNotifications()
        if (notifications == null) {
            notifications = ArrayList()
        }
        notifications.add(newNotification)
        val gson = Gson()
        val arrayGSON = gson.toJson(notifications)
        saveString(NOTIFICATIONS, arrayGSON)
    }

    fun getStoredNotifications(): ArrayList<String>? {
        val g = Gson()
        val type = object : TypeToken<List<String>?>() {}.type
        val notificationsJSON = getString(NOTIFICATIONS)
        notificationsJSON?.let {
            return g.fromJson(it, type)
        }
        return ArrayList()
    }

    fun clearStoredNotifications() {
        val notifications = ArrayList<String>()
        val gson = Gson()
        val arrayGSON = gson.toJson(notifications)
        saveString(NOTIFICATIONS, arrayGSON)
    }
}