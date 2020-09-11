package com.apicci.morsevibrate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    //lateinit var speedEditText: EditText
    public val MAX_SPEED: Int = 9999
    public val MIN_SPEED: Int = 35

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        speedEditText.setText(getSharedPreferences("morseNotify", 0).getInt("speed", 150).toString())
        enabledCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)

        //TODO: Start and stop the service


        //TODO: Option to play sound and flash light in addition to vibration

        //Speed changing
        speedEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        });

        saveSpeedButton.setOnClickListener {
            //Set up SharedPreferences
            val editor = getSharedPreferences("morseNotify", 0).edit()

            editor.putBoolean("enabled", enabledCheckBox.isChecked)

            if(speedEditText.text.toString().length > 5){
                Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

            } else if(speedEditText.text.toString() == null || speedEditText.text.toString().toInt() > MAX_SPEED || speedEditText.text.toString().toInt() < MIN_SPEED){
                Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

            } else {
                //Save speed in SharedPreferences
                editor.putInt("speed", speedEditText.text.toString().toInt())
                editor.apply()
            }
        }

    }

}