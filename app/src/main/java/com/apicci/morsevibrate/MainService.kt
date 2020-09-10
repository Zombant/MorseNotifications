package com.apicci.morsevibrate

import android.app.Service
import android.content.Intent
import android.os.Debug
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast

class MainService : NotificationListenerService() {

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        Toast.makeText(applicationContext, "Notification Detected", Toast.LENGTH_SHORT).show()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

    }

    override fun onListenerConnected() {
        super.onListenerConnected()
    }
}
