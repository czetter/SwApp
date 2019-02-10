package com.example.swapp.data

import com.google.android.gms.maps.model.LatLng

class Park {

    var name: String = ""
    var latLng:LatLng

    constructor(_name: String, _latitude: Double, _longitude: Double) {
        name = _name
        latLng = LatLng(_latitude,_longitude)

    }

}