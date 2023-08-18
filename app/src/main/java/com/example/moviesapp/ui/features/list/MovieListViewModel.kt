package com.example.moviesapp.ui.features.list

import androidx.lifecycle.ViewModel
import com.example.moviesapp.domain.GetFavoriteMoviesUseCase
import com.example.moviesapp.domain.GetLocalMovieListUseCase
import com.example.moviesapp.domain.GetRemoteMovieListUseCase
import com.example.moviesapp.ui.model.BaseUiState
import com.example.moviesapp.ui.model.MovieItemResult
import com.example.moviesapp.util.extensions.applySchedulers
import com.example.moviesapp.util.extensions.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getRemoteMovieListUseCase: GetRemoteMovieListUseCase,
    private val getLocalMovieListUseCase: GetLocalMovieListUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val _moviesUiState: MutableStateFlow<MovieListUiState> =
        MutableStateFlow(MovieListUiState())
    val moviesUiState: StateFlow<MovieListUiState> get() = _moviesUiState.asStateFlow()

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        disposable.add(
            getLocalMovieListUseCase()
                .applySchedulers()
                .doOnSubscribe { _moviesUiState.update { it.copy(isLoading = true) } }
                .subscribe({ result ->
                    if (result.isNullOrEmpty()) {
                        fetchMoviesFromServer()
                    } else {
                        _moviesUiState.update { it.copy(isLoading = false, movieList = result) }
                    }
                }, { exception: Throwable ->
                    handleApiException(exception)
                })
        )
    }

    private fun fetchMoviesFromServer() {
        disposable.add(
            getRemoteMovieListUseCase()
                .applySchedulers()
                .doOnSubscribe { _moviesUiState.update { it.copy(isLoading = true) } }
                .subscribe({ result ->
                    _moviesUiState.update { it.copy(isLoading = false, movieList = result) }
                }, { exception ->
                    handleApiException(exception)
                })
        )
    }

    private fun handleApiException(exception: Throwable) {
        _moviesUiState.update {
            it.copy(
                isLoading = false,
                errorMessage = exception.getErrorMessage(),
                movieList = listOf()
            )
        }
    }

    fun getFavouriteMovies() {
        disposable.add(
            getFavoriteMoviesUseCase()
                .applySchedulers()
                .doOnSubscribe { _moviesUiState.update { it.copy(isLoading = true) } }
                .subscribe({ result ->
                    _moviesUiState.update { it.copy(isLoading = false, movieList = result) }
                }, { exception: Throwable ->
                    handleApiException(exception = exception)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun refresh() {
        getAllMovies()
    }
}


data class MovieListUiState(
    override var isLoading: Boolean = false,
    override var errorMessage: String? = null,
    val movieList: List<MovieItemResult> = emptyList(),
) : BaseUiState()