package com.apicci.morsevibrate

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.notification.NotificationListenerService
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: Start and stop the service
        startServiceButton.setOnClickListener(View.OnClickListener {
            println("------------------Start Clicked")
        })

        stopServiceButton.setOnClickListener(View.OnClickListener{
            println("------------------Stop Clicked")
        })

    }

}