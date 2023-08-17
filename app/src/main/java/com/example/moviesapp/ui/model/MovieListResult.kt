package com.example.moviesapp.ui.model

import com.example.moviesapp.util.extensions.IMAGE_URL_PREFIX

data class MovieItemResult(
    val backdropPath: String = "",
    val id: Int = -1,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = -1
) {
    val finalImageUrl get() = IMAGE_URL_PREFIX.plus(backdropPath)
}