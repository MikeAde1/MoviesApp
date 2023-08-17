package com.example.moviesapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.moviesapp.ui.model.MovieItemResult

@Entity(
    tableName = "cached_movie_list",
    indices = [Index(value = ["id"], unique = true)]
)
data class MovieListEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,

    val backdropPath: String?,
    @ColumnInfo(name = "id")
    val id: Int?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?
) {
    fun toMovieItemResult() = MovieItemResult(
        backdropPath.orEmpty(),
        id ?: -1,
        originalLanguage.orEmpty(),
        originalTitle.orEmpty(),
        overview.orEmpty(),
        popularity ?: 0.0,
        posterPath.orEmpty(),
        releaseDate.orEmpty(),
        title.orEmpty(),
        video ?: false,
        voteAverage ?: 0.0,
        voteCount ?: -1
    )
}