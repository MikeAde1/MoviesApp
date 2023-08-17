package com.example.moviesapp.data.models

import com.example.moviesapp.ui.model.MovieDetailResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailResponse(
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "budget")
    val budget: Int?,
    @Json(name = "genres")
    val genres: List<Genre>?,
    @Json(name = "homepage")
    val homepage: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "original_language")
    val originalLanguage: String?,
    @Json(name = "original_title")
    val originalTitle: String?,
    @Json(name = "overview")
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "revenue")
    val revenue: Int?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "title")
    val title: String?
) {
    fun toMovieDetailResult() = MovieDetailResult(
        backdropPath.orEmpty(),
        budget ?: 0,
        genres.orEmpty(),
        homepage.orEmpty(),
        id ?: -1,
        originalLanguage.orEmpty(),
        originalTitle.orEmpty(),
        overview.orEmpty(),
        posterPath.orEmpty(),
        releaseDate.orEmpty(),
        revenue ?: 0,
        status.orEmpty(),
        title.orEmpty()
    )
}