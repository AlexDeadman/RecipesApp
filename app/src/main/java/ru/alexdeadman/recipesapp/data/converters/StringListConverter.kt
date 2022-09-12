package ru.alexdeadman.recipesapp.data.converters

import androidx.room.ProvidedTypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class StringListConverter(gson: Gson) : ListConverter<String>(gson)