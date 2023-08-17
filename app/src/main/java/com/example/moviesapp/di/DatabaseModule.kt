package com.example.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviesapp.data.local.MovieAppDatabase
import com.example.moviesapp.data.local.datasource.LocalDataSource
import com.example.moviesapp.data.local.daos.MovieListDao
import com.example.moviesapp.data.local.datasource.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource

    companion object {
        private const val DATABASE_NAME = "movies_app.db"

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): MovieAppDatabase {
            return Room.databaseBuilder(
                context,
                MovieAppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun provideMovieListDao(
            movieAppDatabase: MovieAppDatabase
        ): MovieListDao = movieAppDatabase.movieListDao()
    }
}