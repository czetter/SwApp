package com.example.swapp.model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.swapp.R
import com.example.swapp.data.Park
import com.google.android.gms.maps.model.LatLng

class LocationsAdapter(val parkList: MutableList<Park>, val currentLocation: LatLng, private val mContext:Context) :
    RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    private val distanceCalculator = DistanceCalculator()
    private val jsonParser = JsonParser(mContext)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = parkList[position].name
        val distance = distanceCalculator.distanceBetween(LatLng(parkList[position].latitude, parkList[position].longitude), currentLocation)
        val distanceRounded = String.format("%.3f", distance).toDouble()
        holder.txtDistance.text = "ca. $distanceRounded km"
        if (parkList[position].fav) {
            holder.imgFav.setBackgroundResource(R.drawable.ic_star_on)
        } else
            holder.imgFav.setBackgroundResource(R.drawable.ic_star_off)

        holder.imgFav.setOnClickListener {
            parkList[position].fav = !parkList[position].fav
            jsonParser.writeJsonParks()
            if (parkList[position].fav) {
                holder.imgFav.setBackgroundResource(R.drawable.ic_star_on)
            } else
                holder.imgFav.setBackgroundResource(R.drawable.ic_star_off)
        }
    }

    private fun setStar(holder: ViewHolder, boolean: Boolean) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.locationview_item, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return parkList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtDistance = itemView.findViewById<TextView>(R.id.txtDistance)
        val imgFav = itemView.findViewById<ImageView>(R.id.imgFav)

    }

}