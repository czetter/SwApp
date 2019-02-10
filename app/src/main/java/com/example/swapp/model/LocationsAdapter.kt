package com.example.swapp.model

import android.content.Context
import android.support.v4.content.res.TypedArrayUtils.getString
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.swapp.R
import com.example.swapp.data.Park
import com.google.android.gms.maps.model.LatLng
import java.text.FieldPosition

class LocationsAdapter(val parkList: MutableList<Park>,val currentLocation:LatLng): RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    val distanceCalculator= DistanceCalculator()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = parkList[position].name
        var distance = distanceCalculator.distanceInKmBetweenEarthCoordinates(parkList[position].latLng,currentLocation).toString()
        holder.txtDistance.text = "ca. $distance km"
       // holder.txtDistance.text =
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.locationview_item, parent, false)
        return ViewHolder(v)
    }



    override fun getItemCount(): Int {
        return parkList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtDistance = itemView.findViewById<TextView>(R.id.txtDistance)

    }

}