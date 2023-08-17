package com.example.moviesapp.ui.model

import com.example.moviesapp.data.models.Genre
import com.example.moviesapp.util.extensions.IMAGE_URL_PREFIX

data class MovieDetailResult(
    val backdropPath: String = "",
    val budget: Int = 0,
    val genres: List<Genre> = listOf(),
    val homepage: String = "",
    val id: Int = -1,
    val originalLangage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val releaseDate: String = "",
    val revenue: Int = 0,
    val status: String = "",
    val title: String = "",
) {
    val finalImageUrl get() = IMAGE_URL_PREFIX.plus(posterPath)
}