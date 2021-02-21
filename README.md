# ApplicationA
Application that retrieves information from notifications that arrive to the phone and stores them on shared preferences.

The dialog to allow notifications reading will be displayed on the first launch. It is necessary to allow the highest possible amount of notifications to evaluate the app properly.  
If the permissions are not granted, ("Cancel" on dialog), the app will ask for permission next time it launches the MainActivity.

## MainActivity
The NotificationListenerService is started when the app launches, after the permission is given. All the incoming notifications who have title or text are going to be stored automatically on the shared preferences.  
To send a test notification, click on "Send notification" button.  
To clear the stored notifications, click on "Clear stored notifications"

### Extra feature:
If "Update app B" is pressed, current data will be sent to Application B and MainActivity of Application B will be started.


