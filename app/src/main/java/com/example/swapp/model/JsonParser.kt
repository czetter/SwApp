package com.example.swapp.model

import android.content.Context
import android.util.Log
import com.example.swapp.data.Park
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONStringer
import java.io.*
import java.util.*
import kotlin.Comparator



class JsonParser(ct: Context) {

    private var mContext = ct
    private val fileName = "parkLocations.json"
    var parkList: MutableList<Park> = mutableListOf()
    private val gson = Gson()

    fun readJsonParks() {
        var jsonString: String

        var inputStream: InputStream = mContext.assets.open(fileName)
        var size = inputStream.available()
        var buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        jsonString = String(buffer, charset("UTF-8"))
        var jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            var obj = jsonArray.getJSONObject(i)
            parkList.add(gson.fromJson(obj.toString(), Park::class.java))
        }
        sortLocationsByFav()
    }

    private fun sortLocationsByFav() {
        Collections.sort(parkList, object : Comparator<Park> {
            override fun compare(o1: Park, o2: Park): Int {
                return o2.fav.compareTo(o1.fav)
            }

        })
    }

    fun writeJsonParks() {
        var jsonString = ""
        for (i in 0 until parkList.size) {
            jsonString += gson.toJson(parkList[i])
            if (i != parkList.size)
                jsonString += ","
        }
        Log.d("JSONSTRING",jsonString)
        val filePath = mContext.filesDir.path.toString() + "/"+fileName
        val f = File(filePath)
        f.writeText(jsonString)


    }
}