package com.example.swapp.database

import android.util.Log
import com.example.swapp.data.Park
import com.google.firebase.database.*


class DatabaseManager {
    var parkList: MutableList<Park> = mutableListOf()
    private lateinit var database: DatabaseReference

    fun loadParks() {

        database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list  = dataSnapshot.value   //ez eg yjson string
                Log.d("FAST",list.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("DBSM", "Failed to retrieve data")
            }
        })


    }
}