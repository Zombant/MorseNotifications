package com.apicci.morsevibrate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.packages_list_row.view.*

class PackagesAdapter(private val data: ArrayList<PackageEntry>, private val dataFiltered: ArrayList<PackageEntry>, private val context: Context?) : RecyclerView.Adapter<PackagesAdapter.PackagesViewHolder>(), Filterable{

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
        val currentItem = dataFiltered[position]


        //Populate respective view holder with the data
        holder.imageView.setImageDrawable(context?.packageManager?.getApplicationIcon(currentItem.appPackage))
        holder.textView.text = currentItem.appName

        //Load the packages from the SharedPreferences and convert to loadedArray, ArrayList<String>
        val gson: Gson = Gson()
        val json = context?.getSharedPreferences("morseNotify", 0)?.getString("packages",null)
        val loadedArray: ArrayList<String>
        if(json != null) {
            loadedArray = gson.fromJson(json, ArrayList<String>()::class.java)
        } else {
            loadedArray = ArrayList<String>()
        }
        //Set the checkbox if the loaded array already contains that checkbox's package
        holder.checkBox.isChecked = loadedArray.contains(currentItem.appPackage)

        holder.checkBox.setOnClickListener {

            //Load the packages from the SharedPreferences and convert to loadedArray, ArrayList<String>
            val gson: Gson = Gson()
            val json = context?.getSharedPreferences("morseNotify", 0)?.getString("packages",null)
            val loadedArray: ArrayList<String>
            if(json != null) {
                loadedArray = gson.fromJson(json, ArrayList<String>()::class.java)
            } else {
                loadedArray = ArrayList<String>()
            }

            //If the checkbox is changed to checked, add it to the loadedArray
            if(holder.checkBox.isChecked) {
                loadedArray.add(currentItem.appPackage)
                currentItem.checked = true
                holder.setIsRecyclable(false)
            }

            //If the checkbox is unchecked, remove it from loadedArray
            if(!holder.checkBox.isChecked) {
                loadedArray.remove(currentItem.appPackage)
                currentItem.checked = false
            }

            //Save changed loadedArray
            val editor = context?.getSharedPreferences("morseNotify", 0)?.edit()
            editor?.putString("packages", gson.toJson(loadedArray))
            editor?.apply()

            //Clear shared preferences for testing
            //context?.getSharedPreferences("morseNotify", 0)?.edit()?.remove("packages")?.commit()
            Log.d("------------", context?.getSharedPreferences("morseNotify", 0)?.getString("packages","No Packages"))
        }
    }

    override fun getItemCount(): Int {
        return dataFiltered.size
    }


    class PackagesViewHolder(rowInstance: View) : RecyclerView.ViewHolder(rowInstance) {
        val imageView: ImageView = rowInstance.packageIconImageView
        val textView: TextView = rowInstance.packageNameTextView
        val checkBox: CheckBox = rowInstance.packageCheckBox
    }

    //Override method that returns an anonymous filter object
    override fun getFilter(): Filter {
        return object : Filter(){

            //Accepts the text from the search bar from PackageSelectActivity and returns an updated list of packages
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                //Empty arraylist to store the filtered packages
                val filteredPackages: ArrayList<PackageEntry> = ArrayList()

                //If the search bar is null or empty, don't filter (copy all of data to filteredList)
                if(constraint == null || constraint.isEmpty()){
                    filteredPackages.addAll(data)
                } else {
                    //Create the filter from the search
                    val filterSearch: String = constraint.toString().toLowerCase().trim()

                    //For each package in the unfiltered list of packages
                    for(item in data){
                        //If the package name contains the search query, add it to the list of filtered packages
                        if(item.appName.toLowerCase().contains(filterSearch)){
                            filteredPackages.add(item)
                        }
                    }
                }
                //Create a FilterResults and give it the list of filtered packages
                val results = FilterResults()
                results.values = filteredPackages

                //Results get sent to below publishResults method as 'results'
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                //Clear everything from dataFiltered
                dataFiltered.clear()

                //Add the results from the performFiltering method to the dataFiltered
                dataFiltered.addAll(results?.values as ArrayList<PackageEntry>)

                //Update recyclerview
                notifyDataSetChanged()
            }

        }
    }

}