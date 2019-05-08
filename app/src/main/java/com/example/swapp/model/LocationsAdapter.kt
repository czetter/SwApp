package com.example.swapp.model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.swapp.data.Park
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.util.Log
import android.widget.ImageView
import com.example.swapp.R
import com.example.swapp.fragments.RatingFragment
import kotlin.collections.ArrayList


class LocationsAdapter(val parkList: ArrayList<Park>, val supportFragmentManager: FragmentManager) :
    RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var actual = parkList[position]
        holder.txtName.text = actual.name
        val distance = actual.distance
        holder.txtDistance.text = "ca. $distance km"
        holder.txtRating.text = actual.calculateRating().toString()

        holder.btnDirection.setOnClickListener { v ->
            val uri = "google.navigation:q=" + actual.latitude + "," + actual.longitude
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            v.context.startActivity(intent)
        }
        holder.imgStar.setOnClickListener {
            showDialog(actual.name)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.swapp.R.layout.locationview_item, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return parkList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtDistance = itemView.findViewById<TextView>(R.id.txtDistance)
        val btnDirection = itemView.findViewById<ImageView>(R.id.directions_button)
        val txtRating = itemView.findViewById<TextView>(R.id.txtRating)
        val imgStar = itemView.findViewById<ImageView>(R.id.imgStar)


    }

    private fun showDialog(parkname:String) {
        var dialog = RatingFragment()
        var bundle = Bundle()
        bundle.putString("name",parkname)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "RatingDialog")
    }

}