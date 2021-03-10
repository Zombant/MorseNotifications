package com.apicci.morsevibrate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_package_select.*
import kotlinx.android.synthetic.main.package_list_fragment.*

class PackageSelectActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val fragment: PackageListFragment = PackageListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_select)

        //Convert bundle from MainActivity int Arraylist<PackageEntry>
        val receivedBundle = intent.extras?.getBundle("packageExtra")
        val data = receivedBundle?.getSerializable("data") as ArrayList<PackageEntry>

        //Convert ArrayList back into a bundle
        val bundleOut = Bundle()
        bundleOut.putSerializable("data", data)

        //Give the fragment the bundle
        fragment.arguments = bundleOut

        //Apply the fragment to the FrameLayout
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }

    }

    override fun onStop() {
        super.onStop()

        //Toast to tell the user that the app selection was saved (only so they know the app selection is saved automatically, not with the button)
        Toast.makeText(applicationContext, "App Selection Saved", Toast.LENGTH_SHORT).show()
    }

    //Inflate the search bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.package_select_activity_menu, menu)

        //Get the search menu item
        val searchMenuItem = menu?.findItem(R.id.app_bar_search)

        //Get the search view from the search menu item
        val searchView = searchMenuItem?.actionView as SearchView

        //Set the hint for the search view
        searchView.queryHint = "Search for an app..."
        searchView.setOnQueryTextListener(this)

        return true
    }

    //When the text in the search view is changed
    override fun onQueryTextChange(newText: String?): Boolean {

        //Get a reference to the recyclerview's adapter
        val adapter = fragment.recyclerView.adapter as PackagesAdapter

        //Call the filter method and give it the text in the search bar
        adapter.filter.filter(newText)

        return true
    }

    //When the text in the search view is submitted, unused except close keyboard
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }



}