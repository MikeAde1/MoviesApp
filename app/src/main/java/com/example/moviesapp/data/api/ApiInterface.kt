package com.example.moviesapp.data.api

import com.example.moviesapp.data.models.MovieDetailResponse
import com.example.moviesapp.data.models.MovieListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("3/discover/movie")
    fun getMovieList(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Single<MovieListResponse>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Single<MovieDetailResponse>

    @GET("3/account/{account_id}/favorite/movies")
    fun getFavouriteMovies(@Path("account_id") accountId: Int = 20288710): Single<MovieListResponse>
}

//const val sessionId = "8b9d4c50e61510c9ead2990b06ea06ff36cbfd5d"
