package com.apicci.morsevibrate

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.ToneGenerator
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Telephony
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson

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
        //println("------------------Notification Detected")

        if(getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)) {


            //Make sure the vibrator object is not null
            if (vibrator == null) {
                //Get phone vibrator
                vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            //Get audio manager service
            val audiomanager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            //Tone generator
            val toneGen: ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

            //Whether vibration and/or sound should be used
            val shouldVibrate: Boolean = getSharedPreferences("morseNotify", 0).getBoolean("shouldVibrate", false)
            val shouldSound: Boolean = getSharedPreferences("morseNotify", 0).getBoolean("shouldSound", false)
            Log.d("--------------", shouldVibrate.toString() + shouldSound.toString())

            //Load the packages from the SharedPreferences and convert to loadedArray, ArrayList<String>
            var gson: Gson = Gson()
            var json = getSharedPreferences("morseNotify", 0)?.getString("packages",null)
            var loadedArray: ArrayList<String>
            if(json != null) {
                loadedArray = gson.fromJson(json, ArrayList<String>()::class.java)
            } else {
                loadedArray = ArrayList<String>()
            }

            //To get deault sms app
            //Telephony.Sms.getDefaultSmsPackage(applicationContext)


            //Test if the notification came from an app that is in the loadedArray
            for (i in loadedArray) {
                if (sbn?.packageName.equals(i)) {

                    //Save volume and set to max
                    val currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)

                    //TODO: Vibrate App Name

                    //Vibrate Notification Title
                    if (getSharedPreferences("morseNotify", 0).getBoolean("vibrateTitle", false)) {
                        vibrateMessage(sbn?.notification?.extras?.getString("android.title"), getSharedPreferences("morseNotify", 0).getInt("speed", -1), -1, shouldVibrate, shouldSound, toneGen)
                        Thread.sleep(300)
                    }
                    //Vibrate the message in the text field of the notification
                    vibrateMessage(sbn?.notification?.extras?.getString("android.text"), getSharedPreferences("morseNotify", 0).getInt("speed", -1), getSharedPreferences("morseNotify", 0).getInt("maxWords", -1), shouldVibrate, shouldSound, toneGen)

                    //Set volume back to previous
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
                }
            }



        }

    }

    //Called when a notification is cleared
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    //Vibrate a dot
    private fun vibrateDot(multiplier: Int, shouldVibrate: Boolean, shouldSound: Boolean, toneGen: ToneGenerator){
        if(Build.VERSION.SDK_INT >= 26) {
            if (shouldVibrate) vibrator?.vibrate(VibrationEffect.createOneShot(multiplier.toLong() * 1, VibrationEffect.DEFAULT_AMPLITUDE))
            if (shouldSound) toneGen.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, (multiplier.toLong() * 1).toInt());


            //TODO: or try SystemClock.sleep(mills)
            Thread.sleep(multiplier.toLong() * 1)
        }
    }
    //Vibrate a dash
    private fun vibrateDash(multiplier: Int, shouldVibrate: Boolean, shouldSound: Boolean, toneGen: ToneGenerator){
        if(Build.VERSION.SDK_INT >= 26) {
            if (shouldVibrate) vibrator?.vibrate(VibrationEffect.createOneShot(multiplier.toLong() * 3, VibrationEffect.DEFAULT_AMPLITUDE))
            if (shouldSound) toneGen.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, (multiplier.toLong() * 3).toInt())
            Thread.sleep(multiplier.toLong() * 3)
        }

    }

    //Wait between vibrations
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
    private fun vibrateMessage(message: String?, multiplier: Int, maxWords: Int, shouldVibrate: Boolean, shouldSound: Boolean, toneGen: ToneGenerator){

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
                vibrateDot(multiplier, shouldVibrate, shouldSound, toneGen)
                spaceBetweenSymbols(multiplier)
            }
            if(vibrationCode[symbol] == '-'){
                vibrateDash(multiplier, shouldVibrate, shouldSound, toneGen)
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


