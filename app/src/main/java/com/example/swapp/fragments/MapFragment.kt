package com.example.swapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.swapp.R
import com.example.swapp.data.Park
import com.example.swapp.model.DistanceCalculator
import com.example.swapp.model.JsonParser
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private val zoom = 15F

    private val LOCATION_REQUEST_CODE: Int = 1
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var mView: View
    private lateinit var mContext: Context

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var jsonParser: JsonParser
    private lateinit var currentLocation: LatLng
    private var nearestPark = Park("DefaultPark", 0.0, 0.0, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        jsonParser = JsonParser(mContext)
        jsonParser.readJsonParks()

        createLocationRequest()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        saveLocation(location)
                        makeMySnackbar(location)

                        currentLocation = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoom))
                    }

                }
            }
        }


    }

    fun makeMySnackbar(location: Location) {
        if (nearestPark.name != getNearestPark(location).name) {
            nearestPark = getNearestPark(location)
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "${nearestPark.name} van a legközelebb. ", Snackbar.LENGTH_INDEFINITE
            ).setAction("Útvonal") {
                val uri = "google.navigation:q=" + nearestPark.latitude + "," + nearestPark.longitude
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context?.startActivity(intent)
            }.show()
        }

    }


    fun saveLocation(location: Location) {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.mSharedPref), Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.locationSavelat), location.latitude.toString())
            putString(getString(R.string.locationSavelong), location.longitude.toString())
            commit()
        }
    }

    private fun getNearestPark(location: Location): Park {
        var distanceCalculator = DistanceCalculator()

        var nearest = jsonParser.parkList[0]
        var distance = distanceCalculator.distanceBetween(
            LatLng(location.latitude, location.longitude),
            LatLng(nearest.latitude, nearest.longitude)
        )
        for (i in 1 until jsonParser.parkList.size) {
            var tmp = distanceCalculator.distanceBetween(
                LatLng(location.latitude, location.longitude),
                LatLng(jsonParser.parkList[i].latitude, jsonParser.parkList[i].longitude)
            )
            if (tmp < distance) {
                distance = tmp
                nearest = jsonParser.parkList[i]
            }
        }
        return nearest

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null)
            mContext = context
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission())
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPause() {
        super.onPause()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = map
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_map, container, false)
        return mView
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.setOnMyLocationButtonClickListener(this)   //no function yet
        mMap.setOnMyLocationClickListener(this)         //no function yet
        enableMyLocation()
        setLocation()
        addMarkers()

    }

    private fun setLocation() {
        if (checkPermission()) {
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoom))
                    }
                }
        }
    }

    private fun enableMyLocation() {
        if (checkPermission()) {
            mMap.isMyLocationEnabled = true
            Log.i("MapFragmet", "LOCATION ENABLED!")
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Log.i("MapsFragment", "MyLocation button clicked")
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Log.i("MapsFragment", "Current location:\n$p0")
    }

    private fun checkPermission(): Boolean {       //checks pesmission for ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                enableMyLocation()
            } else {
                Toast.makeText(mContext, "ACCESS_FINE_LOCATION permission is not granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(mContext)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            Log.i("LocationRequestONSUCCES", locationSettingsResponse.toString())
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        activity,
                        1
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun addMarkers() {
        for (i in 0 until jsonParser.parkList.size) {
            var actual = jsonParser.parkList[i]
            mMap.addMarker(MarkerOptions().position(LatLng(actual.latitude, actual.longitude)).title(actual.name))
        }
    }

}