package com.example.moviesapp.data.local

import com.squareup.moshi.Moshi

object MoviesAppMoshi {
    fun getMoshi(): Moshi = Moshi.Builder().build()
}