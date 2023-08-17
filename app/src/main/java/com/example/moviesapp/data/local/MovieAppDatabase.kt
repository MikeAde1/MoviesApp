package com.example.moviesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.data.local.daos.MovieListDao
import com.example.moviesapp.data.local.model.MovieListEntity

@Database(
    entities = [MovieListEntity::class],
    version = 2,
    exportSchema = false
)

abstract class MovieAppDatabase : RoomDatabase() {
    abstract fun movieListDao(): MovieListDao
}
