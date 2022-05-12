package ru.mirea.trainscheduler.repository.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapToStringConverter {
    @TypeConverter
    fun stringToMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {
        }.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun mapToString(map: Map<String, String>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}