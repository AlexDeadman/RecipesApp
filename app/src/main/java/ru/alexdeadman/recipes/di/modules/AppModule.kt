package ru.alexdeadman.recipes.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipes.data.recipes.RecipeListRepository
import ru.alexdeadman.recipes.data.recipes.room.RecipesDatabase
import ru.alexdeadman.recipes.ui.ViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: RecipeListRepository): ViewModelFactory =
        ViewModelFactory(repository)

    @Provides
    @Singleton
    fun provideRecipesDatabase(@ApplicationContext context: Context): RecipesDatabase =
        Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_database"
        ).build()
}