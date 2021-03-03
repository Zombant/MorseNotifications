package com.apicci.morsevibrate

import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.package_list_fragment.*

class MainActivity : AppCompatActivity() {

    private val MAX_SPEED: Int = 1000
    private val MIN_SPEED: Int = 35

    private val MAX_NUMOFWORDS: Int = 10000
    private val MIN_NUMOFWORDS: Int = 1



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if(id == R.id.about_menu_item){
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()

        //If Access is not granted, alert the user
        if(!getNotificationsEnabled()){
            val alert = enableNotificationAccessAlert()
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
        enabledSwitch.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)
        vibrateTitleSwitch.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("vibrateTitle", false)
        vibrateAppNameSwitch.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("vibrateAppName", false)
        maxWordsSwitch.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("maxWordsChecked", false)
        maxWordsEditText.isEnabled = maxWordsSwitch.isChecked
        maxWordsTextView.isEnabled = maxWordsSwitch.isChecked


        //TODO: Option to play sound and flash light in addition to vibration

        //When the maxWords checkbox is toggled
        maxWordsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            maxWordsEditText.isEnabled = isChecked
            maxWordsTextView.isEnabled = maxWordsSwitch.isChecked
        }

        //When the save button is pressed
        saveSpeedButton.setOnClickListener {

            //Alert the user to enable Notification Access if it is not already, otherwise continue as normal
            if(!getNotificationsEnabled()){
                //Show alert dialog that directs user to settings
                val alertDialog = enableNotificationAccessAlert()
                alertDialog.show()

            } else {
                Log.d("------------", "test")
                //Set up SharedPreferences
                val editor = getSharedPreferences("morseNotify", 0).edit()

                if(/*speedEditText.text.toString().length > 5 || */speedEditText.text.toString() == ""){
                    Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

                } else if(maxWordsSwitch.isChecked && (/*maxWordsEditText.text.toString().length > 8 || */maxWordsEditText.text.toString() == "")){
                    Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

                } else if(speedEditText.text.toString().toInt() > MAX_SPEED || speedEditText.text.toString().toInt() < MIN_SPEED){
                    Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

                } else if(maxWordsSwitch.isChecked && (maxWordsEditText.text.toString().toInt() > MAX_NUMOFWORDS || maxWordsEditText.text.toString().toInt() < MIN_NUMOFWORDS)) {
                    Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

                } else {
                    //Save speed in SharedPreferences
                    editor.putBoolean("enabled", enabledSwitch.isChecked)
                    editor.putInt("speed", speedEditText.text.toString().toInt())
                    editor.putBoolean("maxWordsChecked", maxWordsSwitch.isChecked)

                    if(maxWordsSwitch.isChecked) {
                        editor.putInt("maxWords", maxWordsEditText.text.toString().toInt())
                    } else {
                        editor.putInt("maxWords", -1)
                    }

                    editor.putBoolean("vibrateTitle", vibrateTitleSwitch.isChecked)
                    editor.putBoolean("vibrateAppName", vibrateAppNameSwitch.isChecked)

                    editor.apply()
                }
            }
        }

        //Select apps button
        button.setOnClickListener {
            val dialog = PackageListDialogFragment()

            val bundle = Bundle()

            val data = getInstalledApps()
            for(i in data.indices){
                Log.d("-------------", data[i].appName)
            }
            bundle.putSerializable("data", data)

            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "PackageDialog")
        }

    }

    //Returns true if Notification Access in enabled
    private fun getNotificationsEnabled(): Boolean{
        //Get String of all enabled notification listeners on device
        val allListeners: String = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")

        //Return true if this package is contained in the list of listeners
        return allListeners.contains(packageName)

    }

    //Creates an alert that directs the user to the Notification Access settings
    private fun enableNotificationAccessAlert(): AlertDialog{
        val alertDialogBuilder = AlertDialog.Builder(this)
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

    private fun getInstalledApps(): ArrayList<PackageEntry> {
        //Create empty list
        var data = ArrayList<PackageEntry>()

        //Get a list of all packages
        val packages = packageManager.getInstalledPackages(0)
        for (currentPackage in packages.indices) {
            //If the current package is a system package
            if (packages[currentPackage]!!.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val appName = packages[currentPackage].applicationInfo.loadLabel(packageManager).toString()
                val icon = packages[currentPackage].applicationInfo.icon
                val checked = false
                val item = PackageEntry(appName, icon, checked)
                data.add(item)
            }
        }

        return data
    }

}
