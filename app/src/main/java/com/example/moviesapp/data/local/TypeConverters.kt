package com.example.moviesapp.data.local

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Types
import java.io.IOException

object DbTypeConverter {
    @TypeConverter
    @JvmStatic
    fun genreListToString(list: List<Int>?): String? =
        getMoshiAdapterForGenre().toJson(list)

    @TypeConverter
    @JvmStatic
    fun stringToGenreList(jsonString: String) =
        getMoshiAdapterForGenre().fromNullableJson(jsonString)

    private fun getMoshiAdapterForGenre(): JsonAdapter<List<Int>> {
        val type = Types.newParameterizedType(List::class.java, Int::class.java)
        return MoviesAppMoshi.getMoshi().adapter(type)
    }
}

fun <T> JsonAdapter<T>.fromNullableJson(string: String?): T? =
    try {
        fromJson(string ?: "")
    } catch (e: JsonDataException) {
        null
    } catch (e1: IOException) {
        null
    }

fun <T> JsonAdapter<T>.toNonNullableJson(data: T?): String =
    try {
        toJson(data)
    } catch (e: JsonDataException) {
        ""
    } catch (e1: IOException) {
        ""
    }