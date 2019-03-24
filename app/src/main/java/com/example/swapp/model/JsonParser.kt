package com.example.swapp.model

import android.content.Context
import com.example.swapp.data.Park
import com.google.gson.Gson
import org.json.JSONArray
import java.io.InputStream
import java.util.*


class JsonParser(context: Context) {

    private var mContext = context
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



    }


}