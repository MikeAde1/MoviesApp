package com.example.moviesapp.util.extensions

import androidx.lifecycle.MutableLiveData
import com.example.moviesapp.ui.model.BaseUiState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T, R : BaseUiState> Single<T>.applyProgressBar(uiState: MutableLiveData<R>) =
    doOnSubscribe { uiState.value?.apply { isLoading = true } }
        .doOnEvent { _, _ -> uiState.value?.apply { isLoading = false } }

fun <T> Single<T>.applySchedulers() =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())