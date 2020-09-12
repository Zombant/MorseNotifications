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

        if(getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)) {


            //Make sure the vibrator object is not null
            if (vibrator == null) {
                //Get phone vibrator
                vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            //Debugging stuff
            println(sbn?.notification?.extras?.getString("android.title"))
            println(sbn?.notification?.extras?.getString("android.text"))
            println(sbn?.packageName)
            println(Telephony.Sms.getDefaultSmsPackage(applicationContext))

            //Only run if the notification came from the default SMS app
            if (sbn?.packageName == Telephony.Sms.getDefaultSmsPackage(applicationContext)) {
                //Vibrate the message in the text field of the notification
                vibrateMessage(sbn?.notification?.extras?.getString("android.text"), getSharedPreferences("morseNotify", 0).getInt("speed", -1), getSharedPreferences("morseNotify", 0).getInt("maxWords", -1))
            }
        }

    }

    //Called when a notification is cleared
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }


    private fun vibrateDot(multiplier: Int){
        if(Build.VERSION.SDK_INT >= 26) {
            vibrator?.vibrate(VibrationEffect.createOneShot(multiplier.toLong() * 1, VibrationEffect.DEFAULT_AMPLITUDE))
            //TODO: or try SystemClock.sleep(mills)
            Thread.sleep(100)
        }
    }
    private fun vibrateDash(multiplier: Int){
        if(Build.VERSION.SDK_INT >= 26) {
            vibrator?.vibrate(VibrationEffect.createOneShot(multiplier.toLong() * 3, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(multiplier.toLong() * 3)
        }
    }
    private fun spaceBetweenSymbols(multiplier: Int){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(multiplier.toLong() * 1)
        }
    }
    private fun spaceBetweenLetters(multiplier: Int){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(multiplier.toLong() * 3)
        }
    }
    private fun spaceBetweenWords(multiplier: Int){
        if(Build.VERSION.SDK_INT >= 26) {
            Thread.sleep(multiplier.toLong() * 7)
        }
    }

    //Vibrates depending on the message given
    private fun vibrateMessage(message: String?, multiplier: Int, maxWords: Int){

        if(multiplier == -1){return}

        //Translate the message into morse code
        var vibrationCode: String? = MorseTranslate.TextToMorse(message)

        //Debugging stuff
        println(message)
        println(vibrationCode)

        //Do not continue if the message is empty
        if(vibrationCode == null){ return }

        var currentWordCount: Int = maxWords

        //Vibrate depending on the translated message
        for(symbol in vibrationCode.indices){
            println()
            if(vibrationCode[symbol] == '.'){
                vibrateDot(multiplier)
                spaceBetweenSymbols(multiplier)
            }
            if(vibrationCode[symbol] == '-'){
                vibrateDash(multiplier)
                spaceBetweenSymbols(multiplier)
            }
            if(vibrationCode[symbol] == 'l'){
                spaceBetweenLetters(multiplier)
            }
            if(vibrationCode[symbol] == 's'){
                spaceBetweenWords(multiplier)
                currentWordCount--
            }
            if (maxWords != -1 && currentWordCount <= 0) {
                break
            }
        }
    }

}


