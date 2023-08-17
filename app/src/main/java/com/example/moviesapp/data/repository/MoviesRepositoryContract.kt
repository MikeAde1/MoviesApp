package com.example.moviesapp.data.repository

import com.example.moviesapp.ui.model.MovieDetailResult
import com.example.moviesapp.ui.model.MovieItemResult
import io.reactivex.Single

interface MoviesRepositoryContract {
    fun getLocalMoviesList(): Single<List<MovieItemResult>>
    fun getMovieDetail(movieId: Int): Single<MovieDetailResult>
    fun getFavouriteMovies(): Single<List<MovieItemResult>>
    fun getMovieListFromServer(): Single<List<MovieItemResult>>
}