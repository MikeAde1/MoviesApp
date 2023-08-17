package com.example.moviesapp.ui.features.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.domain.GetFavoriteMoviesUseCase
import com.example.moviesapp.domain.GetLocalMovieListUseCase
import com.example.moviesapp.domain.GetRemoteMovieListUseCase
import com.example.moviesapp.ui.model.BaseUiState
import com.example.moviesapp.ui.model.MovieItemResult
import com.example.moviesapp.util.extensions.applyProgressBar
import com.example.moviesapp.util.extensions.applySchedulers
import com.example.moviesapp.util.extensions.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getRemoteMovieListUseCase: GetRemoteMovieListUseCase,
    private val getLocalMovieListUseCase: GetLocalMovieListUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val _moviesUiState: MutableLiveData<MovieListUiState> =
        MutableLiveData(MovieListUiState())
    val moviesUiState: LiveData<MovieListUiState> get() = _moviesUiState

    init {
        getAllMovies()
    }
    fun getAllMovies() {
        disposable.add(
            getLocalMovieListUseCase()
                .applySchedulers()
                .applyProgressBar(_moviesUiState)
                .subscribe({ result ->
                    if (result.isNullOrEmpty()) {
                        fetchMoviesFromServer()
                    } else {
                        _moviesUiState.value = moviesUiState.value?.copy(movieList = result)
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
                .applyProgressBar(_moviesUiState)
                .subscribe({ result ->
                    _moviesUiState.value = moviesUiState.value?.copy(movieList = result)
                }, { exception ->
                    handleApiException(exception)
                })
        )
    }

    private fun handleApiException(exception: Throwable) {
        _moviesUiState.value =
            moviesUiState.value?.copy(
                errorMessage = exception.getErrorMessage(),
                movieList = listOf()
            )
    }

    fun getFavouriteMovies() {
        disposable.add(
            getFavoriteMoviesUseCase()
                .applySchedulers()
                .applyProgressBar(_moviesUiState)
                .subscribe({ result ->
                    _moviesUiState.value = moviesUiState.value?.copy(movieList = result)
                }, { exception: Throwable ->
                    _moviesUiState.value =
                        moviesUiState.value?.copy(
                            errorMessage = exception.getErrorMessage(),
                            movieList = listOf()
                        )
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}


data class MovieListUiState(
    override var isLoading: Boolean = false,
    override var errorMessage: String? = null,
    val movieList: List<MovieItemResult> = emptyList(),
) : BaseUiState()