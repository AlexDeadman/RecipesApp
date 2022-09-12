package ru.alexdeadman.recipesapp.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


abstract class ListConverter<T> (private val gson: Gson) {

    @TypeConverter
    fun toString(value: List<T>): String = gson.toJson(value)

    @TypeConverter
    fun toList(value: String): List<T> =
        gson.fromJson(value, object : TypeToken<List<T>>() {}.type)
}