package com.example.moviesapp.di

import android.content.Context
import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.util.AndroidResourceProvider
import com.example.moviesapp.util.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesAppModule {

    companion object {
        @Provides
        @ViewModelScoped
        fun provideResourceProvider(@ApplicationContext appContext: Context): ResourceProvider =
            AndroidResourceProvider(appContext)
    }
    @Binds
    abstract fun bindMoviesRepository(moviesRepository: MoviesRepository): MoviesRepositoryContract
}