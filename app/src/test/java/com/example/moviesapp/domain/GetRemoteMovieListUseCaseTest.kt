package com.example.moviesapp.domain

import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.ui.model.MovieItemResult
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetRemoteMovieListUseCaseTest {

    private lateinit var useCase: GetRemoteMovieListUseCase
    private val moviesRepository: MoviesRepositoryContract = mock()

    @Before
    fun setUp() {
        useCase = GetRemoteMovieListUseCase(moviesRepository)
    }

    @Test
    fun `invoke should return list of remote movies`() {
        val movieListResult = listOf(
            MovieItemResult(id = 1, title = "Movie Title 1"),
            MovieItemResult(id = 2, title = "Movie Title 2")
        )
        whenever(moviesRepository.getMovieListFromServer()).thenReturn(Single.just(movieListResult))

        val testObserver = useCase().test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.size == 2 && it[0].title == "Movie Title 1" }
    }

    @Test
    fun `invoke should return error`() {
        val error = Throwable("Error fetching remote movie list")
        whenever(moviesRepository.getMovieListFromServer()).thenReturn(Single.error(error))
        val testObserver = useCase().test()

        testObserver.assertError(error)
    }
}