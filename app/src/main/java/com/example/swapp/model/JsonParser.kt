package com.example.swapp.model

import android.content.Context
import android.util.Log
import com.example.swapp.data.Park
import org.json.JSONArray
import java.io.InputStream
import java.io.UTFDataFormatException

class JsonParser(val ct: Context) {

    var mContext = ct
    val fileName = "parkLocations.json"
    var parkList :MutableList<Park> = mutableListOf()

    fun parseLocations() {
        var jsonString: String

        var inputStream: InputStream = mContext.assets.open(fileName)
        var size = inputStream.available()
        var buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        jsonString= String(buffer, charset("UTF-8"))
        var jsonArray = JSONArray(jsonString)

        for(i in 0 until jsonArray.length()){
            var obj = jsonArray.getJSONObject(i)
            var name = obj.getString("name")
            var latitude = obj.getDouble("latitude")
            var longitude = obj.getDouble("longitude")
            parkList.add(Park(name,latitude,longitude))
        }
    }
}