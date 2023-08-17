package com.example.moviesapp.util.extensions

import java.util.Locale


const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/w500"

fun String.capitalize() = this.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
}
