package ru.alexdeadman.recipesapp.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


// TODO pass Gson
abstract class ListConverter<T> {


    @TypeConverter
    fun toString(value: List<T>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<T> =
        Gson().fromJson(value, object : TypeToken<List<T>>() {}.type)
}