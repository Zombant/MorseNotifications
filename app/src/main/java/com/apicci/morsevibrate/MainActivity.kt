package com.apicci.morsevibrate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    public val MAX_SPEED: Int = 1000
    public val MIN_SPEED: Int = 35

    public val MAX_NUMOFWORDS: Int = 10000
    public val MIN_NUMOFWORDS: Int = 1


    override fun onResume() {
        super.onResume()

        //If Access is not granted, alert the user
        if(!getNotificationsEnabled()){
            var alert = enableNotificationAccessAlert()
            alert.show()
            Log.d("-----------------", "SHOWALERT")
        }
        Log.d("-----------------", "RESUMED")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup
        speedEditText.setText(getSharedPreferences("morseNotify", 0).getInt("speed", 150).toString())
        maxWordsEditText.setText(getSharedPreferences("morseNotify", 0).getInt("maxWords", 100).toString())
        if(getSharedPreferences("morseNotify", 0).getInt("maxWords", 100) == -1) maxWordsEditText.setText("")
        enabledCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)
        vibrateTitleCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("vibrateTitle", false)
        vibrateAppNameCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("vibrateAppName", false)
        maxWordsCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("maxWordsChecked", false)
        maxWordsEditText.isEnabled = maxWordsCheckBox.isChecked
        maxWordsTextView.isEnabled = maxWordsCheckBox.isChecked


        //TODO: Option to play sound and flash light in addition to vibration

        //When the checkbox is toggled
        maxWordsCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                maxWordsEditText.isEnabled = isChecked
                maxWordsTextView.isEnabled = maxWordsCheckBox.isChecked
            }

        })

        //When the save button is pressed
        saveSpeedButton.setOnClickListener {

            //Alert the user to enable Notification Access if it is not already, otherwise continue as normal
            if(!getNotificationsEnabled()){
                //Show alert dialog that directs user to settings
                var alertDialog = enableNotificationAccessAlert()
                alertDialog.show()

            } else {
                Log.d("------------", "test")
                //Set up SharedPreferences
                val editor = getSharedPreferences("morseNotify", 0).edit()

                if(/*speedEditText.text.toString().length > 5 || */speedEditText.text.toString() == ""){
                    Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

                } else if(maxWordsCheckBox.isChecked && (/*maxWordsEditText.text.toString().length > 8 || */maxWordsEditText.text.toString() == "")){
                    Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

                } else if(speedEditText.text.toString().toInt() > MAX_SPEED || speedEditText.text.toString().toInt() < MIN_SPEED){
                    Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

                } else if(maxWordsCheckBox.isChecked && (maxWordsEditText.text.toString().toInt() > MAX_NUMOFWORDS || maxWordsEditText.text.toString().toInt() < MIN_NUMOFWORDS)) {
                    Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

                } else {
                    //Save speed in SharedPreferences
                    editor.putBoolean("enabled", enabledCheckBox.isChecked)
                    editor.putInt("speed", speedEditText.text.toString().toInt())
                    editor.putBoolean("maxWordsChecked", maxWordsCheckBox.isChecked)

                    if(maxWordsCheckBox.isChecked) {
                        editor.putInt("maxWords", maxWordsEditText.text.toString().toInt())
                    } else {
                        editor.putInt("maxWords", -1)
                    }

                    editor.putBoolean("vibrateTitle", vibrateTitleCheckBox.isChecked)
                    editor.putBoolean("vibrateAppName", vibrateAppNameCheckBox.isChecked)

                    editor.apply()
                }
            }
        }

    }

    //Returns true if Notification Access in enabled
    fun getNotificationsEnabled(): Boolean{
        //Get String of all enabled notification listeners on device
        var allListeners: String = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")

        //Return true if this package is contained in the list of listeners
        return allListeners.contains(packageName)

    }

    //Creates an alert that directs the user to the Notification Access settings
    fun enableNotificationAccessAlert(): AlertDialog{
        var alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Notification Access")
        alertDialogBuilder.setMessage("App requires Notification Access to be turned on in the settings")
        alertDialogBuilder.setPositiveButton("Go to Settings"){dialog, which ->
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
        alertDialogBuilder.setNegativeButton("Exit App") { dialog, which ->
            finishAffinity()
        }

        return alertDialogBuilder.create()
    }

}
