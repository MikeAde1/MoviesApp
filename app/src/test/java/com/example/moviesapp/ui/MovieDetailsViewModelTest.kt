package com.example.moviesapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.moviesapp.domain.GetMovieDetailsUseCase
import com.example.moviesapp.ui.features.details.MovieDetailsUiState
import com.example.moviesapp.ui.features.details.MovieDetailsViewModel
import com.example.moviesapp.ui.model.MovieDetailResult
import com.example.moviesapp.util.extensions.GENERIC_ERROR_MESSAGE
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class MovieDetailsViewModelTest {
    @get:Rule
    var rxRule = RxSchedulersOverrideRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieDetailsViewModel

    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mock()

    private val liveDataObserver = mock<Observer<Any>>()

    private val fakeErrorMessage = "An error occurred"
    private val jsonResponse = "{\"status_message\":\"$fakeErrorMessage\"}"
    private val errorResponseBody =
        jsonResponse.toResponseBody("application/json".toMediaTypeOrNull())
    private val fakeErrorResponse = Response.error<String>(400, errorResponseBody)
    private val movieId = 100

    private val movieDetailResult = MovieDetailResult()

    @Before
    fun setUp() {
        whenever(getMovieDetailsUseCase.invoke(movieId)).thenReturn(Single.just(movieDetailResult))
    }

    @Test
    fun `show error message when api call to get movies details returns http exception`() {
        whenever(getMovieDetailsUseCase.invoke(movieId)).thenReturn(
            Single.error(
                HttpException(
                    fakeErrorResponse
                )
            )
        )
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)

        viewModel.movieDetailsUiState.observeForever(liveDataObserver)
        viewModel.getMovieDetails(movieId)
        verify(getMovieDetailsUseCase).invoke(movieId)
        verify(liveDataObserver).onChanged(
            MovieDetailsUiState(isLoading = false, errorMessage = fakeErrorMessage)
        )
    }

    @Test
    fun `show error message when call to get movie details returns non http exception`() {
        whenever(getMovieDetailsUseCase.invoke(movieId)).thenReturn(
            Single.error(
                UnknownHostException()
            )
        )
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)
        viewModel.movieDetailsUiState.observeForever(liveDataObserver)
        viewModel.getMovieDetails(movieId)
        verify(getMovieDetailsUseCase).invoke(movieId)
        verify(liveDataObserver).onChanged(
            MovieDetailsUiState(
                isLoading = false, errorMessage = GENERIC_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `display favorite movies in UI if movies if call to fetch favourite movies is successful`() {
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)

        viewModel.movieDetailsUiState.observeForever(liveDataObserver)
        viewModel.getMovieDetails(movieId)

        verify(getMovieDetailsUseCase).invoke(movieId)
        verify(liveDataObserver).onChanged(MovieDetailsUiState(movieDetail = movieDetailResult))
    }
}