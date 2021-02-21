package com.facundojaton.applicationa

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facundojaton.applicationa.databinding.ActivityMainBinding
import com.facundojaton.applicationa.services.NotificationListener
import com.facundojaton.applicationa.utils.sendNotification


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null
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
        val rs = contentResolver.query(
            AProvider.CONTENT_URI,
            arrayOf(AProvider._ID, AProvider.NAME, AProvider.MEANING), null, null, null
        )

        binding.buttonNext.setOnClickListener {
            if (rs?.moveToNext()!!) {
                binding.etName.setText(rs.getString(1))
                binding.etMeaning.setText(rs.getString(2))
            }
        }

        binding.buttonUpdate.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = "com.facundojaton.sharedata"
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "*/*"
                val data = ArrayList<String>()
                data.add("Facundo")
                data.add("Pass")
                putExtra("Object", data)
            }
            startActivity(sendIntent)
        }

        binding.buttonPrevious.setOnClickListener {
            if (rs?.moveToPrevious()!!) {
                binding.etName.setText(rs.getString(1))
                binding.etMeaning.setText(rs.getString(2))
            }
        }

        binding.button.setOnClickListener {
            val notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
            val timestamp = System.currentTimeMillis() / 1000
            notificationManager.sendNotification(
                getString(R.string.notification_sent) + " with timestamp: $timestamp", this
            )
        }

        binding.button2.setOnClickListener {

        }

        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog?.show()
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.facundojaton.applicationa")
        registerReceiver(imageChangeBroadcastReceiver, intentFilter)
    }

    /**
     * Change Intercepted Notification Image
     * Changes the MainActivity image based on which notification was intercepted
     * @param notificationCode The intercepted notification code
     */
    private fun changeInterceptedNotificationImage(notificationCode: Int) {
        if (notificationCode == NotificationListener.InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE){
            binding.textView.text = "Notification Received"
        } else {
            binding.textView.text = "Notification Received but couldn't handle it very well"
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
     * Image Change Broadcast Receiver.
     * We use this Broadcast Receiver to notify the Main Activity when
     * a new notification has arrived, so it can properly change the
     * notification image
     */
    inner class ImageChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val receivedNotificationCode = intent.getIntExtra("Notification Code", -1)
            changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.service_name)
        alertDialogBuilder.setMessage("message from dialog")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, id ->
            startActivity(
                Intent(
                    ACTION_NOTIFICATION_LISTENER_SETTINGS
                )
            )
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, id ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return alertDialogBuilder.create()
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(imageChangeBroadcastReceiver)
    }

}