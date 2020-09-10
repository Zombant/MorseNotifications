package com.apicci.morsevibrate

import android.content.Intent
import android.os.*
import android.provider.Telephony
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast
import kotlin.reflect.typeOf

class MainService() : NotificationListenerService() {

    private var vibrator: Vibrator? = null

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Service Stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        println("------------------Notification Detected")
        if(vibrator == null){
                vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        println(sbn?.notification?.extras?.getString("android.title"))
        println(sbn?.notification?.extras?.getString("android.text"))
        println(sbn?.packageName)
        println(Telephony.Sms.getDefaultSmsPackage(applicationContext))
        if(sbn?.packageName == Telephony.Sms.getDefaultSmsPackage(applicationContext)){
            vibrateMessage(sbn?.notification?.extras?.getString("android.text"))
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

    }

    private fun vibrateDot(){
        if(Build.VERSION.SDK_INT >= 26) {
            //TODO: Make the WPM based on user input
            vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            //TODO: or try SystemClock.sleep(mills)
            Thread.sleep(100)
        }
    }
    private fun vibrateDash(){
        if(Build.VERSION.SDK_INT >= 26) {
            //TODO: Make the WPM based on user input
            vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(300)
        }
    }
    private fun spaceBetweenSymbols(){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(100)
        }
    }
    private fun spaceBetweenLetters(){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(300)
        }
    }
    private fun spaceBetweenWords(){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(700)
        }
    }

    private fun vibrateMessage(message: String?){
        var vibrationCode: String? = MorseTranslate.TextToMorse(message)
        println(message)
        println(vibrationCode)

        if(vibrationCode == null){ return }

        for(symbol in vibrationCode.indices){
            println()
            if(vibrationCode[symbol] == '.'){
                vibrateDot()
                spaceBetweenSymbols()
            }
            if(vibrationCode[symbol] == '-'){
                vibrateDash()
                spaceBetweenSymbols()
            }
            if(vibrationCode[symbol] == 'l'){
                spaceBetweenLetters()
            }
            if(vibrationCode[symbol] == 's'){
                spaceBetweenWords()
            }
        }
    }

}


