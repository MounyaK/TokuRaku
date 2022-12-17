package com.kamyart.tokuraku.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ArrayConverter {
    @TypeConverter
    fun fromArray(array : ArrayList<String>): String = Gson().toJson(array)

    @TypeConverter
    fun toArray(json: String): ArrayList<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(json, listType)
    }
}

class  MapConverter{
    @TypeConverter
    fun fromMap(map : Map<String, String>): String = Gson().toJson(map)

    @TypeConverter
    fun toMap(json: String): Map<String, String> {
        val listType: Type = object : TypeToken<Map<String?, String?>?>() {}.type
        return Gson().fromJson(json, listType)
    }
}