package com.example.moviesapp.domain

import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.ui.model.MovieDetailResult
import io.reactivex.Single
import javax.inject.Inject

class GetMovieDetailsUseCase@Inject constructor(
    private val moviesRepositoryContract: MoviesRepositoryContract
) {
    operator fun invoke(movieId: Int): Single<MovieDetailResult> {
        return moviesRepositoryContract.getMovieDetail(movieId)
    }
}