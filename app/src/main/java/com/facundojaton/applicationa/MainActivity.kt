package com.facundojaton.applicationa

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facundojaton.applicationa.databinding.ActivityMainBinding
import com.facundojaton.applicationa.services.NotificationListener
import com.facundojaton.applicationa.utils.AppSharedPreferences
import com.facundojaton.applicationa.utils.sendNotification


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // private var notificationBroadcastReceiver: NotificationBroadcastReceiver? = null
    private var enableNotificationListenerAlertDialog: AlertDialog? = null

    companion object {
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIfApplicationBIsStartingForResult(intent)

        binding.btnUpdate.setOnClickListener {
            sendDataToApplicationB()
        }

        binding.btnSendNotification.setOnClickListener {
            sendTestNotification()
        }

        binding.btnClearSP.setOnClickListener {
            AppSharedPreferences(applicationContext).clearStoredNotifications()
        }

        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog?.show()
        }
    }

    private fun sendTestNotification() {
        waitingMode(true)
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        val timestamp = System.currentTimeMillis() / 1000
        notificationManager.sendNotification(
            getString(R.string.notification_sent) + " with timestamp: $timestamp", this
        )
        waitingMode(false)
    }

    private fun sendDataToApplicationB() {
        val sendIntent: Intent = Intent().apply {
            action = "com.facundojaton.sharedata"
            type = "*/*"
            val data = ArrayList<String>()
            val storedNotifications =
                AppSharedPreferences(this@MainActivity).getStoredNotifications()
            if (!storedNotifications.isNullOrEmpty()) {
                for (notification in storedNotifications) {
                    data.add(notification)
                }
            } else data.add("There aren't notifications to retrieve")
            putExtra("Object", data)
        }
        startActivity(sendIntent)
    }

    private fun checkIfApplicationBIsStartingForResult(intent: Intent?) {
        if (intent?.action == "com.facundojaton.getdata") {
            Intent("com.facundojaton.getdata", Uri.parse("content://result_uri")).also { result ->
                result.type = "*/*"
                val data = ArrayList<String>()
                val storedNotifications =
                    AppSharedPreferences(this@MainActivity).getStoredNotifications()
                if (!storedNotifications.isNullOrEmpty()) {
                    for (notification in storedNotifications) {
                        data.add(notification)
                    }
                } else data.add("There aren't notifications to retrieve")
                result.putExtra("Object", data)
                setResult(Activity.RESULT_OK, result)
            }
            finish()
        }
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private val isNotificationServiceEnabled: Boolean
        private get() {
            val pkgName = packageName
            val flat: String = Settings.Secure.getString(
                contentResolver,
                ENABLED_NOTIFICATION_LISTENERS
            )
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":".toRegex()).toTypedArray()
                for (i in names.indices) {
                    val cn = ComponentName.unflattenFromString(names[i])
                    if (cn != null) {
                        if (TextUtils.equals(pkgName, cn.packageName)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.listen_to_notifications)
        alertDialogBuilder.setMessage("Select which notifications do you want to listen and store")
        alertDialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, id ->
            startActivity(
                Intent(
                    ACTION_NOTIFICATION_LISTENER_SETTINGS
                )
            )
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, id ->
            Toast.makeText(this, getString(R.string.listener_inactive), Toast.LENGTH_SHORT).show()
        }
        return alertDialogBuilder.create()
    }

    private fun waitingMode(isWaiting: Boolean) {
        binding.apply {
            btnClearSP.isEnabled = !isWaiting
            btnUpdate.isEnabled = !isWaiting
            btnSendNotification.isEnabled = !isWaiting
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}