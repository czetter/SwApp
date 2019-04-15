package com.example.swapp.database

import android.content.Context
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
    private var parkList = ArrayList<Park>()

    fun loadParks() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        var parksDatabase = FirebaseDatabase.getInstance()
        var parksDatabaseReference = parksDatabase.getReference("parks")
        parksDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var snapshotIterator = dataSnapshot.children
                var iterator = snapshotIterator.iterator()
                while (iterator.hasNext()) {
                    var snapshot = iterator.next()
                    var currentPark = snapshot.getValue(Park::class.java)
                    if (currentPark != null)
                        parkList.add(currentPark)
                }
                checkSavedData()
                saveData()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }
    fun checkSavedData(){
        var sharedPreferencesParks = getParks()
        if(sharedPreferencesParks != parkList)
            clearSharedPreferences()

    }

    fun upload(list: MutableList<Park>) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("parks")

        for (p in list) {
            var key = dbRef.push().key
            FirebaseDatabase.getInstance().reference.child(key!!).setValue(p)
        }
    }

    private fun saveData() {
        Log.d(TAG, "saveData")
        var sharedPreferences = context.getSharedPreferences("parks", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json = gson.toJson(parkList)
        editor.putString("parkList", json)
        editor.apply()
    }

    fun getParks(): ArrayList<Park> {
        Log.d(TAG,"getParks")
        var sharedPreferences = context.getSharedPreferences("parks", Context.MODE_PRIVATE)
        var gson = Gson()
        var json = sharedPreferences.getString("parkList", "Error")
        if(json == "Error")
            return ArrayList<Park>()
        val type = object : TypeToken<ArrayList<Park>>() {}.type
        return gson.fromJson(json,type) as ArrayList<Park>

    }

    fun clearSharedPreferences(){
        var shp = context.getSharedPreferences("parks", Context.MODE_PRIVATE)
        var editor = shp.edit()
        editor.clear()
        editor.commit()
    }



}