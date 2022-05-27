package ru.mirea.trainscheduler.view.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.model.Location

class LocationAutoCompleteAdapter(
    private val initContext: Context,
    private var locations: List<Location>,
) : ArrayAdapter<Location>(initContext, R.layout.auto_complete_item) {
    fun setLocations(locations: List<Location>) {
        this.locations = locations
    }

    override fun getCount(): Int {
        return locations.size
    }

    override fun getItem(position: Int): Location {
        return locations[position]
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            val inflater = (initContext as Activity).layoutInflater
            convertView = inflater.inflate(R.layout.auto_complete_item, parent, false)
        }
        try {
            val location: Location = getItem(position)
            val locationName = convertView!!.findViewById<View>(R.id.item_name) as TextView
            locationName.text = location.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }
}