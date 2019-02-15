package com.example.swapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import com.example.swapp.model.JsonParser
import com.example.swapp.model.LocationsAdapter
import com.google.android.gms.maps.model.LatLng

class LocationsActivity : AppCompatActivity() {

    private lateinit var jsonParser: JsonParser
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        jsonParser = JsonParser(baseContext)
        jsonParser.readJsonParks()

        val sharedPref = getSharedPreferences(
            getString(R.string.mSharedPref), Context.MODE_PRIVATE) ?: return
        val currentLat = sharedPref.getString(getString(R.string.locationSavelat), "error - default value")
        val currentLong = sharedPref.getString(getString(R.string.locationSavelong),"error - default value")
        val currentLatLng = LatLng(currentLat.toDouble(),currentLong.toDouble())

        viewManager = LinearLayoutManager(this)
        viewAdapter = LocationsAdapter(jsonParser.parkList,currentLatLng,baseContext)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {

            val controller = AnimationUtils.loadLayoutAnimation(context,R.anim.animation_layout)
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            layoutAnimation = controller


        }

    }
}
