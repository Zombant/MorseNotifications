package com.apicci.morsevibrate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.packages_list_row.view.*

class PackagesAdapter(private val data: List<PackageEntry>, private val context: Context?) : RecyclerView.Adapter<PackagesAdapter.PackagesViewHolder>(){

    // Called by recycle view when its time to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        //Create a row instance but do add to RecyclerView yet (the RecyclerView does that)
        val rowInstance = LayoutInflater.from(parent.context).inflate(R.layout.packages_list_row, parent, false)

        //Pass new rowInstance to the View Holder
        return PackagesViewHolder(rowInstance)
    }

    // Populates view holders with data
    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) {
        //Get the corresponding item from data
        val currentItem = data[position]

        //Populate respective view holder with the data
        holder.imageView.setImageDrawable(context?.packageManager?.getApplicationIcon(currentItem.appPackage))
        holder.textView.text = currentItem.appName
        holder.checkBox.isChecked = currentItem.checked
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class PackagesViewHolder(rowInstance: View) : RecyclerView.ViewHolder(rowInstance) {
        val imageView: ImageView = rowInstance.packageIcon
        val textView: TextView = rowInstance.packageNameTextView
        val checkBox: CheckBox = rowInstance.packageCheckBox
    }
}