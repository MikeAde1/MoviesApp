package com.example.moviesapp.data

import com.example.moviesapp.data.api.ApiInterface
import com.example.moviesapp.data.local.datasource.LocalDataSource
import com.example.moviesapp.data.local.model.MovieListEntity
import com.example.moviesapp.data.models.MovieDetailResponse
import com.example.moviesapp.data.models.MovieItemResponse
import com.example.moviesapp.data.models.MovieListResponse
import com.example.moviesapp.data.repository.MoviesRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MovieRepositoryTest {

    private val apiInterface: ApiInterface = mock()
    private val localDataSource: LocalDataSource = mock()

    private lateinit var moviesRepository: MoviesRepository

    private val fakeMovieListEntity = MovieListEntity(
        backdropPath = "backdropPath",
        id = -1,
        originalLanguage = "originalLanguage",
        originalTitle = "originalTitle",
        overview = "overview",
        popularity = 0.0,
        posterPath = "posterPath",
        releaseDate = "releaseDate",
        title = "title",
        video = false,
        voteAverage = 0.0,
        voteCount = -1
    )

    private val fakeMovieItemResponse = MovieItemResponse(
        false,
        "backdropPath",
        listOf(),
        0,
        "originalLanguage",
        "originalTitle",
        "overview",
        0.0,
        "",
        "", "", false, 0.0, 0
    )

    private val fakeMovieListResponse = MovieListResponse(
        page = 1,
        results = listOf(fakeMovieItemResponse),
        totalPages = 2,
        totalResults = 10
    )

    private val fakeMovieDetailResponse = MovieDetailResponse(
        "path", -1, listOf(), "homepage", -1,
        "originalLanguage", "title", "overview",
        "posterPath", "date", 1, "Released", ""
    )

    private val movieId = 1

    @Before
    fun setUp() {
        moviesRepository = MoviesRepository(apiInterface, localDataSource)
    }

    @Test
    fun `getLocalMoviesList should return list of MovieItemResult`() {
        // Mock the behavior of localDataSource
        whenever(localDataSource.getAllMovies()).thenReturn(Single.just(listOf(fakeMovieListEntity)))

        // Call the method to be tested
        val testObserver = moviesRepository.getLocalMoviesList().test()

        // Verify the result
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.isNotEmpty() }
    }

    @Test
    fun `getMovieListFromServer should return list of MovieItemResult`() {
        whenever(apiInterface.getMovieList()).thenReturn(Single.just(fakeMovieListResponse))

        whenever(localDataSource.saveAllMovies(fakeMovieListResponse.results.map {
            it.toMovieItemEntity()
        })).thenReturn(arrayOf(1L))

        val testObserver = moviesRepository.getMovieListFromServer().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.isNotEmpty() }
    }

    @Test
    fun `getMovieDetail should return MovieDetailResult`() {
        whenever(apiInterface.getMovieDetail(movieId)).thenReturn(
            Single.just(
                fakeMovieDetailResponse
            )
        )

        val testObserver = moviesRepository.getMovieDetail(1).test()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun `getFavouriteMovies should return list of MovieItemResult`() {
        whenever(apiInterface.getFavouriteMovies()).thenReturn(Single.just(fakeMovieListResponse))

        val testObserver = moviesRepository.getFavouriteMovies().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.isNotEmpty() }
    }
}