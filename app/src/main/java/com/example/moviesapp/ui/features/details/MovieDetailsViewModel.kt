package com.example.moviesapp.ui.features.details

import androidx.lifecycle.ViewModel
import com.example.moviesapp.domain.GetMovieDetailsUseCase
import com.example.moviesapp.ui.model.BaseUiState
import com.example.moviesapp.ui.model.MovieDetailResult
import com.example.moviesapp.util.extensions.applySchedulers
import com.example.moviesapp.util.extensions.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _movieDetailsUiState: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(
        MovieDetailsUiState()
    )
    val movieDetailsUiState: MutableStateFlow<MovieDetailsUiState> get() = _movieDetailsUiState
    fun getMovieDetails(movieId: Int) {
        disposable.add(
            getMovieDetailsUseCase.invoke(movieId)
                .applySchedulers()
                .doOnSubscribe {
                    _movieDetailsUiState.update { it.copy(isLoading = true) }
                }
                .subscribe({ result ->
                    _movieDetailsUiState.update { it.copy(isLoading = false, movieDetail = result) }
                }, { exception ->
                    _movieDetailsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.getErrorMessage(),
                            movieDetail = null
                        )
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

data class MovieDetailsUiState(
    override var isLoading: Boolean = false,
    override var errorMessage: String? = null,
    val movieDetail: MovieDetailResult? = null
) : BaseUiState()