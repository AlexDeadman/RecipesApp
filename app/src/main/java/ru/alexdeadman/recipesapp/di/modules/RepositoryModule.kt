package ru.alexdeadman.recipesapp.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipesapp.data.recipes.RecipeListLocalDataSource
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRemoteDataSource
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipesApi
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RetrofitRecipeListDataSource
import ru.alexdeadman.recipesapp.data.recipes.room.RecipesDatabase
import ru.alexdeadman.recipesapp.data.recipes.room.RoomRecipeListDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRecipesLocalDataSource(database: RecipesDatabase): RecipeListLocalDataSource =
        RoomRecipeListDataSource(database.recipeListDao())

    @Provides
    @Singleton
    fun provideRecipesRemoteDataSource(api: RecipesApi): RecipeListRemoteDataSource =
        RetrofitRecipeListDataSource(api)

    @Provides
    @Singleton
    fun provideRecipesRepository(
        local: RecipeListLocalDataSource,
        remote: RecipeListRemoteDataSource
    ): RecipeListRepository = RecipeListRepository(local, remote)
}