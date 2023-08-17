package com.example.moviesapp.data.local.datasource

import com.example.moviesapp.data.local.daos.MovieListDao
import com.example.moviesapp.data.local.model.MovieListEntity
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val movieListDao: MovieListDao
) : LocalDataSource {
    override fun getAllMovies(): Single<List<MovieListEntity>> {
        return movieListDao.getAllMovies()
    }
    override fun saveAllMovies(movieList: List<MovieListEntity>): Array<Long> {
        return movieListDao.saveAllMovies(movieList)
    }
}
