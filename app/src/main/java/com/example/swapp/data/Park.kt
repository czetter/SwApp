package com.example.swapp.data

import com.google.android.gms.maps.model.LatLng

class Park {

    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var distance: Double = 0.0
    var votes = mutableListOf<Int>()

    constructor() {
        for (i in 0..4)
            votes.add(0)
    }             //ez kell a Firebasehoz

    constructor(_name: String, _latitude: Double, _longitude: Double) {
        name = _name
        latitude = _latitude
        longitude = _longitude

        for (i in 0..4)
            votes.add(0)

    }

    override fun toString(): String {
        return "PARK.toString(): $name, $latitude, $longitude"
    }

    fun calculateRating(): Double {
        var sum = 0.0
        var count = 0
        if (votes.isEmpty())
            return 0.0
        for (i in 1..5) {
            sum += i * votes[i - 1]
            count += votes[i - 1]
        }
        if (count == 0)
            return 0.0
        var result = sum / count
        return String.format("%.1f", result).toDouble()
    }


}