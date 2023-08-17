package com.example.moviesapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.moviesapp.domain.GetFavoriteMoviesUseCase
import com.example.moviesapp.domain.GetLocalMovieListUseCase
import com.example.moviesapp.domain.GetRemoteMovieListUseCase
import com.example.moviesapp.ui.features.list.MovieListUiState
import com.example.moviesapp.ui.features.list.MovieListViewModel
import com.example.moviesapp.ui.model.MovieItemResult
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

class MovieListViewModelTest {
    @get:Rule
    var rxRule = RxSchedulersOverrideRule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieListViewModel

    private val liveDataObserver = mock<Observer<Any>>()

    private val getRemoteMovieListUseCase: GetRemoteMovieListUseCase = mock()

    private val getLocalMovieListUseCase: GetLocalMovieListUseCase = mock()

    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = mock()

    private val fakeErrorMessage = "An error occurred"
    private val jsonResponse = "{\"status_message\":\"$fakeErrorMessage\"}"
    private val errorResponseBody =
        jsonResponse.toResponseBody("application/json".toMediaTypeOrNull())
    private val fakeErrorResponse = Response.error<String>(400, errorResponseBody)

    @Before
    fun setup() {
        whenever(getLocalMovieListUseCase.invoke()).thenReturn(Single.just(listOf()))
        whenever(getRemoteMovieListUseCase.invoke()).thenReturn(Single.just(listOf()))
    }

    @Test
    fun `show error message when call to fetch all movies from local source returns an exception`() {
        whenever(getLocalMovieListUseCase.invoke()).thenReturn(
            Single.error(UnknownHostException())
        )

        whenever(getLocalMovieListUseCase.invoke()).thenReturn(
            Single.error(UnknownHostException())
        )

        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )
        viewModel.moviesUiState.observeForever(liveDataObserver)

        verify(getLocalMovieListUseCase).invoke()
        verify(liveDataObserver).onChanged(
            MovieListUiState(
                isLoading = false,
                errorMessage = GENERIC_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `fetch movies from server if movies from local source is null or empty`() {
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )

        viewModel.moviesUiState.observeForever(liveDataObserver)

        verify(getLocalMovieListUseCase).invoke()
        verify(getRemoteMovieListUseCase).invoke()
        verify(liveDataObserver).onChanged(MovieListUiState())
    }

    @Test
    fun `display local movies in UI if movies from local source is not null or empty`() {
        whenever(getLocalMovieListUseCase.invoke()).thenReturn(Single.just(listOf(MovieItemResult())))
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )

        viewModel.moviesUiState.observeForever(liveDataObserver)

        verify(getLocalMovieListUseCase).invoke()
        verify(liveDataObserver).onChanged(MovieListUiState(movieList = listOf(MovieItemResult())))
    }


    @Test
    fun `show error message when call to fetch all movies from remote source returns an http exception`() {
        whenever(getRemoteMovieListUseCase.invoke()).thenReturn(
            Single.error(HttpException(fakeErrorResponse))
        )

        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )
        viewModel.moviesUiState.observeForever(liveDataObserver)

        // viewModel.getAllMovies() has been called at initiation point
        verify(getRemoteMovieListUseCase).invoke()
        verify(liveDataObserver).onChanged(
            MovieListUiState(
                isLoading = false,
                errorMessage = fakeErrorMessage
            )
        )
    }

    @Test
    fun `show error message when call to fetch all movies from remote source returns non http exception`() {
        whenever(getRemoteMovieListUseCase.invoke()).thenReturn(Single.error(UnknownHostException()))
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )
        viewModel.moviesUiState.observeForever(liveDataObserver)

        verify(getRemoteMovieListUseCase).invoke()
        verify(liveDataObserver).onChanged(
            MovieListUiState(
                isLoading = false,
                errorMessage = GENERIC_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `show error message when call to get favourite movies returns http exception`() {
        whenever(getFavoriteMoviesUseCase.invoke()).thenReturn(
            Single.error(
                HttpException(
                    fakeErrorResponse
                )
            )
        )
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )
        viewModel.moviesUiState.observeForever(liveDataObserver)
        viewModel.getFavouriteMovies()
        verify(getFavoriteMoviesUseCase).invoke()
        verify(liveDataObserver).onChanged(
            MovieListUiState(
                isLoading = false,
                errorMessage = fakeErrorMessage
            )
        )
    }

    @Test
    fun `show error message when call to get favourite movies returns non http exception`() {
        whenever(getFavoriteMoviesUseCase.invoke()).thenReturn(Single.error(UnknownHostException()))
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )
        viewModel.moviesUiState.observeForever(liveDataObserver)
        viewModel.getFavouriteMovies()
        verify(getFavoriteMoviesUseCase).invoke()
        verify(liveDataObserver).onChanged(
            MovieListUiState(
                isLoading = false,
                errorMessage = GENERIC_ERROR_MESSAGE
            )
        )
    }

    @Test
    fun `display favorite movies in UI if movies if call to fetch favourite movies is successful`() {
        whenever(getFavoriteMoviesUseCase.invoke()).thenReturn(Single.just(listOf(MovieItemResult())))
        viewModel = MovieListViewModel(
            getRemoteMovieListUseCase,
            getLocalMovieListUseCase,
            getFavoriteMoviesUseCase
        )

        viewModel.moviesUiState.observeForever(liveDataObserver)
        viewModel.getFavouriteMovies()

        verify(getFavoriteMoviesUseCase).invoke()
        verify(liveDataObserver).onChanged(MovieListUiState(movieList = listOf(MovieItemResult())))
    }
}
