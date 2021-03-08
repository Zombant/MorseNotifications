package com.apicci.morsevibrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.package_list_fragment.view.*

//DialogFragment where the user can select apps
class PackageListFragment: Fragment() {

    //Returns the view that will be shown
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Use the package_list_fragment layout
        var rootView: View = inflater.inflate(R.layout.package_list_fragment, container, false)

        //Deserialize the data from the bundle that was passed into the DialogFragment
        val data = arguments!!.getSerializable("data") as ArrayList<PackageEntry>

        // Set the Adapter of the RecyclerView that is in the root view (R.layout.package_list_fragment)
        // to the custom Adapter and pass in the data and application context
        rootView.recyclerView.adapter = PackagesAdapter(data, activity?.applicationContext)

        //Set the layout manager of the RecyclerView to a LinearLayoutManager
        rootView.recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)


        rootView.recyclerView.setHasFixedSize(false)


        return rootView

    }

}