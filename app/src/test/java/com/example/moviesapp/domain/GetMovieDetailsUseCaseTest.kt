package com.example.moviesapp.domain

import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.ui.model.MovieDetailResult
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetMovieDetailsUseCaseTest {
    private lateinit var useCase: GetMovieDetailsUseCase
    private val moviesRepository: MoviesRepositoryContract = mock()

    @Before
    fun setUp() {
        useCase = GetMovieDetailsUseCase(moviesRepository)
    }

    @Test
    fun `invoke should return movie details`() {
        val movieId = 123
        val movieDetailResult = MovieDetailResult(id = 123, title = "Movie Title")
        whenever(moviesRepository.getMovieDetail(movieId)).thenReturn(Single.just(movieDetailResult))

        val testObserver = useCase(movieId).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.id == 123 && it.title == "Movie Title" }
    }

    @Test
    fun `invoke should return error`() {
        val movieId = 123
        val error = Throwable("Error fetching movie details")
        whenever(moviesRepository.getMovieDetail(movieId)).thenReturn(Single.error(error))
        val testObserver = useCase(movieId).test()
        testObserver.assertError(error)
    }
}