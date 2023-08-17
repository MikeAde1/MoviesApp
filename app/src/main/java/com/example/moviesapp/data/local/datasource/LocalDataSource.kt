package com.example.moviesapp.data.local.datasource

import com.example.moviesapp.data.local.model.MovieListEntity
import io.reactivex.Single


interface LocalDataSource {
    fun getAllMovies(): Single<List<MovieListEntity>>
    fun saveAllMovies(movieList: List<MovieListEntity>): Array<Long>
}
