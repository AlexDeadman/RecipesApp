package ru.alexdeadman.recipesapp.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository
import ru.alexdeadman.recipesapp.data.recipes.room.RecipesDatabase
import ru.alexdeadman.recipesapp.ui.ViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

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