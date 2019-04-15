package com.example.swapp.data

import com.google.android.gms.maps.model.LatLng

class Park {

    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
     var fav :Boolean = false
     var distance: Double = 0.0

    constructor(){}             //ez kell a Firebasehoz
    constructor(_name: String, _latitude: Double, _longitude: Double, _fav : Boolean) {
        name = _name
        latitude = _latitude
        longitude = _longitude
        fav = _fav
    }

    override fun toString(): String {
        return "PARK.toString(): $name, $latitude, $longitude"
    }

}