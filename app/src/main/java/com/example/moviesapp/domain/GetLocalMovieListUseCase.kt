package com.example.moviesapp.domain

import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.ui.model.MovieItemResult
import io.reactivex.Single
import javax.inject.Inject

class GetLocalMovieListUseCase@Inject constructor(
    private val moviesRepositoryContract: MoviesRepositoryContract
) {
    operator fun invoke(): Single<List<MovieItemResult>> {
        return moviesRepositoryContract.getLocalMoviesList()
    }
}