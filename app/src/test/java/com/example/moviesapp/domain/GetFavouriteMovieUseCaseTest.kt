package com.example.moviesapp.domain

import com.example.moviesapp.data.repository.MoviesRepositoryContract
import com.example.moviesapp.ui.model.MovieItemResult
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetFavouriteMovieUseCaseTest {
    private lateinit var useCase: GetFavoriteMoviesUseCase
    private val moviesRepository: MoviesRepositoryContract = mock()

    @Before
    fun setUp() {
        useCase = GetFavoriteMoviesUseCase(moviesRepository)
    }
    @Test
    fun `invoke should return list of favorite movies`() {
        // Mock the behavior of the repository
        val movieList = listOf(MovieItemResult())
        whenever(moviesRepository.getFavouriteMovies()).thenReturn(Single.just(movieList))

        // Call the use case
        val testObserver = useCase().test()

        // Verify the result
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue { it.size == 1 }
    }

    @Test
    fun `invoke should return error`() {
        val error = Throwable("Error fetching favorite movies")
        whenever(moviesRepository.getFavouriteMovies()).thenReturn(Single.error(error))

        val testObserver = useCase().test()

        testObserver.assertError(error)
    }
}