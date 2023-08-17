package com.example.moviesapp.data.repository

import com.example.moviesapp.data.api.ApiInterface
import com.example.moviesapp.data.local.datasource.LocalDataSource
import com.example.moviesapp.data.models.MovieItemResponse
import com.example.moviesapp.ui.model.MovieDetailResult
import com.example.moviesapp.ui.model.MovieItemResult
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val localDataSource: LocalDataSource
) : MoviesRepositoryContract {
    override fun getLocalMoviesList(): Single<List<MovieItemResult>> {
        return localDataSource.getAllMovies().map { list ->
            list.map { it.toMovieItemResult() }
        }
    }

    override fun getMovieListFromServer(): Single<List<MovieItemResult>> {
        val response = apiInterface.getMovieList()
            .doOnSuccess { saveMovies(it.results) }
        return response.map { list -> list.results.map { it.toMovieItemResult() } }
    }
    private fun saveMovies(movieList: List<MovieItemResponse>): Array<Long> {
        return localDataSource.saveAllMovies(movieList.map { it.toMovieItemEntity() })
    }

    override fun getMovieDetail(movieId: Int): Single<MovieDetailResult> {
        return apiInterface.getMovieDetail(movieId).map { it.toMovieDetailResult() }
    }

    override fun getFavouriteMovies(): Single<List<MovieItemResult>> {
        return apiInterface.getFavouriteMovies().map {
                response -> response.results.map { it.toMovieItemResult() }
        }
    }
}