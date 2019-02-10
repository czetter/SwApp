package com.example.swapp.model

import com.google.android.gms.maps.model.LatLng
import kotlin.math.PI

class DistanceCalculator {

    private fun degreesToRadians(degrees:Double): Double {
        return degrees * PI / 180
    }

    fun distanceInKmBetweenEarthCoordinates(loc1:LatLng,loc2:LatLng):Double {
        var earthRadiusKm = 6371

        var dLat = degreesToRadians(loc2.latitude-loc1.latitude)
        var dLon = degreesToRadians(loc2.longitude-loc1.longitude)

        var lat1 = degreesToRadians(loc1.latitude)
        var lat2 = degreesToRadians(loc2.latitude)

        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        return earthRadiusKm * c
    }
}