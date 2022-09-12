package ru.alexdeadman.recipesapp.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipesapp.data.converters.StringListConverter
import ru.alexdeadman.recipesapp.data.recipes.room.RecipesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStringListConverter(gson: Gson): StringListConverter = StringListConverter(gson)

    @Provides
    @Singleton
    fun provideRecipesDatabase(
        @ApplicationContext context: Context,
        converter: StringListConverter
    ): RecipesDatabase = Room
        .databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_database"
        )
        .addTypeConverter(converter)
        .build()
}