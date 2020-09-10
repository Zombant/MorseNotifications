package com.apicci.morsevibrate

import android.content.Intent
import android.os.*
import android.provider.Telephony
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast
import kotlin.reflect.typeOf

//NotificationListenerService turns on automatically if
class MainService() : NotificationListenerService() {

    //Reference to device vibrator
    private var vibrator: Vibrator? = null

    //Unused
    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    //Unused- service always runs
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    //Unused- service always runs
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Service Stopped", Toast.LENGTH_SHORT).show()
    }

    //Runs when a notification appears
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        println("------------------Notification Detected")

        //Make sure the vibrator object is not null
        if(vibrator == null){
            //Get phone vibrator
            vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        //Debugging stuff
        println(sbn?.notification?.extras?.getString("android.title"))
        println(sbn?.notification?.extras?.getString("android.text"))
        println(sbn?.packageName)
        println(Telephony.Sms.getDefaultSmsPackage(applicationContext))

        //Only run if the notification came from the default SMS app
        if(sbn?.packageName == Telephony.Sms.getDefaultSmsPackage(applicationContext)){
            //Vibrate the message in the text field of the notification
            vibrateMessage(sbn?.notification?.extras?.getString("android.text"))
        }
    }

    //Called when a notification is cleared
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

    //Vibrates depending on the message given
    private fun vibrateMessage(message: String?){

        //Translate the message into morse code
        var vibrationCode: String? = MorseTranslate.TextToMorse(message)

        //Debugging stuff
        println(message)
        println(vibrationCode)

        //Do not continue if the message is empty
        if(vibrationCode == null){ return }

        //Vibrate depending on the translated message
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


