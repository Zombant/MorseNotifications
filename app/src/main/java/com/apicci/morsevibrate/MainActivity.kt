package com.apicci.morsevibrate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    public val MAX_SPEED: Int = 1000
    public val MIN_SPEED: Int = 35

    public val MAX_NUMOFWORDS: Int = 10000
    public val MIN_NUMOFWORDS: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        speedEditText.setText(getSharedPreferences("morseNotify", 0).getInt("speed", 150).toString())
        maxWordsEditText.setText(getSharedPreferences("morseNotify", 0).getInt("maxWords", 100).toString())
        enabledCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("enabled", false)
        maxWordsCheckBox.isChecked = getSharedPreferences("morseNotify", 0).getBoolean("maxWords", false)


        //TODO: Option to play sound and flash light in addition to vibration

        //TODO: Guide user to settings to activate

        maxWordsCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

            }

        })

        saveSpeedButton.setOnClickListener {
            //Set up SharedPreferences
            val editor = getSharedPreferences("morseNotify", 0).edit()

            if(speedEditText.text.toString().length > 5 || speedEditText.text.toString() == ""){
                Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

            } else if(speedEditText.text.toString().length > 8 || maxWordsEditText.text.toString() == ""){
                Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

            } else if(speedEditText.text.toString().toInt() > MAX_SPEED || speedEditText.text.toString().toInt() < MIN_SPEED){
                Toast.makeText(applicationContext, "Invalid Speed: Please enter a value $MIN_SPEED-$MAX_SPEED", Toast.LENGTH_LONG).show()

            } else if(maxWordsEditText.text.toString().toInt() > MAX_NUMOFWORDS || maxWordsEditText.text.toString().toInt() < MIN_NUMOFWORDS) {
                Toast.makeText(applicationContext, "Invalid Value: Please enter a value $MIN_NUMOFWORDS-$MAX_NUMOFWORDS", Toast.LENGTH_LONG).show()

            } else {
                //Save speed in SharedPreferences
                editor.putBoolean("enabled", enabledCheckBox.isChecked)
                editor.putInt("speed", speedEditText.text.toString().toInt())
                editor.putInt("maxWords", maxWordsEditText.text.toString().toInt())
                editor.apply()
            }
        }

    }

}