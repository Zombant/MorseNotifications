package com.apicci.morsevibrate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startServiceButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show();
            startService(Intent(this, MainService::class.java));
        })

    }
}