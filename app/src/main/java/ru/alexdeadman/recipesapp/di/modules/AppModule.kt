package ru.alexdeadman.recipesapp.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository
import ru.alexdeadman.recipesapp.ui.list.ListViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideListViewModelFactory(repository: RecipeListRepository): ListViewModelFactory =
        ListViewModelFactory(repository)
}