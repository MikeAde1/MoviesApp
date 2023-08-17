package com.example.moviesapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.data.local.model.MovieListEntity
import io.reactivex.Single

@Dao
interface MovieListDao {
    @Query("select * from cached_movie_list")
    fun getAllMovies(): Single<List<MovieListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllMovies(movieList: List<MovieListEntity>): Array<Long>

}