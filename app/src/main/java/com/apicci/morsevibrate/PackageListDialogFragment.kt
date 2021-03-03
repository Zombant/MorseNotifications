package com.apicci.morsevibrate

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.package_list_fragment.view.*
import kotlin.reflect.typeOf

class PackageListDialogFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView: View = inflater.inflate(R.layout.package_list_fragment, container, true)
        rootView.layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        val data = arguments?.getSerializable("data") as ArrayList<PackageEntry>
        Log.d("--------", data.toString())
        for(i in data.indices){
            Log.d("-------------", data[i].appName)
        }
        rootView.recyclerView.adapter = PackagesAdapter(data, activity?.applicationContext)
        rootView.recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        rootView.recyclerView.setHasFixedSize(true)

        return rootView

    }

}