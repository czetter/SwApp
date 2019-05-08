package com.example.swapp.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.swapp.data.Park
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


class DatabaseManager(val context: Context) {
    val TAG = "DBSM"
    val REF = "parks"
    var loading = true
    private var parkList = ArrayList<Park>()

    fun loadParks() {

        var parksDatabase = FirebaseDatabase.getInstance()
        var parksDatabaseReference = parksDatabase.getReference(REF)
        parksDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                clearSharedPreferences()
                var snapshotIterator = dataSnapshot.children
                var iterator = snapshotIterator.iterator()
                while (iterator.hasNext()) {
                    var snapshot = iterator.next()
                    var currentPark = snapshot.getValue(Park::class.java)
                    if (currentPark != null) {
                        parkList.add(currentPark)
                    }

                }

                saveData()
                loading = false
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    private fun saveData() {
        Log.d(TAG, "saveData")
        var sharedPreferences = context.getSharedPreferences(REF, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json = gson.toJson(parkList)
        editor.putString("parkList", json)
        editor.apply()
    }

    fun getParks(): ArrayList<Park> {
        Log.d(TAG, "getParks")
        var sharedPreferences = context.getSharedPreferences(REF, Context.MODE_PRIVATE)
        var gson = Gson()
        var json = sharedPreferences.getString("parkList", "Error")
        if (json == "Error")
            return ArrayList<Park>()
        val type = object : TypeToken<ArrayList<Park>>() {}.type
        return gson.fromJson(json, type) as ArrayList<Park>

    }

    fun clearSharedPreferences() {
        var sharedPreferences = context.getSharedPreferences(REF, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.remove("parkList")
        editor.apply()
    }

    fun addRating(num: Int, parkName: String) {
        var parksDatabase = FirebaseDatabase.getInstance()
        var parksDatabaseReference = parksDatabase.reference.child(REF)
        parksDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var snapshotIterator = dataSnapshot.children
                var iterator = snapshotIterator.iterator()
                while (iterator.hasNext()) {
                    var snapshot = iterator.next()
                    var currentPark = snapshot.getValue(Park::class.java)
                    if (currentPark != null)
                        if (currentPark.name == parkName) {
                            currentPark.votes[num - 1]++
                            var key = snapshot.key
                            parksDatabaseReference.child(key!!).setValue(currentPark)
                        }

                }
                loadParks()

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    fun refresh() {
        clearSharedPreferences()
        loadParks()
    }

    fun addPark(newPark: Park) {
        var parksDatabase = FirebaseDatabase.getInstance()
        var parksDatabaseReference = parksDatabase.reference.child(REF)

        var key = parksDatabaseReference.push().key
        parksDatabaseReference.child(key!!).setValue(newPark)
    }


}