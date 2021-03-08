package com.apicci.morsevibrate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_package_select.*

class PackageSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_select)

        //Convert bundle from MainActivity int Arraylist<PackageEntry>
        val receivedBundle = intent.extras?.getBundle("packageExtra")
        val data = receivedBundle?.getSerializable("data") as ArrayList<PackageEntry>

        //Convert ArrayList back into a bundle
        val bundleOut = Bundle()
        bundleOut.putSerializable("data", data)

        //Create a new fragment and give it the bundle
        val fragment = PackageListFragment()
        fragment.arguments = bundleOut

        //Apply the fragment to the FrameLayout
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }

        backButton.setOnClickListener {
            finish()
        }

    }

    override fun onStop() {
        super.onStop()

        //Toast to tell the user that the app selection was saved (only so they know the app selection is saved automatically, not with the button)
        Toast.makeText(applicationContext, "App Selection Saved", Toast.LENGTH_LONG).show()
    }

}