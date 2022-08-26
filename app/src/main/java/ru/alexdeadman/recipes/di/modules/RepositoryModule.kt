package ru.alexdeadman.recipes.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.recipes.data.recipes.RecipeListLocalDataSource
import ru.alexdeadman.recipes.data.recipes.RecipeListRemoteDataSource
import ru.alexdeadman.recipes.data.recipes.RecipeListRepository
import ru.alexdeadman.recipes.data.recipes.retrofit.RecipesApi
import ru.alexdeadman.recipes.data.recipes.retrofit.RetrofitRecipeListDataSource
import ru.alexdeadman.recipes.data.recipes.room.RecipesDatabase
import ru.alexdeadman.recipes.data.recipes.room.RoomRecipeListDataSource
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