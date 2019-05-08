package com.example.swapp

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import com.example.swapp.data.Park
import com.example.swapp.database.DatabaseManager
import com.example.swapp.fragments.RatingFragment
import com.example.swapp.model.DistanceCalculator
import com.example.swapp.model.LocationsAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_locations.*
import java.util.*
import kotlin.collections.ArrayList


class LocationsActivity : AppCompatActivity(), RatingFragment.Communicator {


    private lateinit var databaseManager: DatabaseManager
    private var parkList = ArrayList<Park>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val distanceCalculator = DistanceCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        databaseManager = DatabaseManager(baseContext)
        parkList = databaseManager.getParks()


        val sharedPref = getSharedPreferences(
            getString(R.string.mSharedPref), Context.MODE_PRIVATE
        ) ?: return
        val currentLat = sharedPref.getString(getString(R.string.locationSavelat), "error - default value")
        val currentLong = sharedPref.getString(getString(R.string.locationSavelong), "error - default value")
        val currentLatLng = LatLng(currentLat.toDouble(), currentLong.toDouble())

        calculateDistances(currentLatLng)
        sortLocationsByDistance()

        viewManager = LinearLayoutManager(this)
        viewAdapter = LocationsAdapter(parkList, supportFragmentManager)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {

            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.animation_layout)
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            layoutAnimation = controller


        }
        swipeRefresh.setOnRefreshListener {
           finish()
            startActivity(intent)

        }

    }


    private fun sortLocationsByDistance() {
        Collections.sort(parkList, object : Comparator<Park> {
            override fun compare(o1: Park, o2: Park): Int {
                return o1.distance.compareTo(o2.distance)
            }

        })
    }

    private fun calculateDistances(currentLatLng: LatLng) {
        for (park in parkList) {
            var distance = distanceCalculator.distanceBetween(LatLng(park.latitude, park.longitude), currentLatLng)
            park.distance = String.format("%.3f", distance).toDouble()
        }
    }

    override fun onRatingPicked(num: Int, parkname: String) {
        Snackbar.make(window.decorView.rootView, "You rated $parkname to $num Stars", Snackbar.LENGTH_LONG).show()
        databaseManager.addRating(num,parkname)
        viewAdapter.notifyDataSetChanged()
    }
}
