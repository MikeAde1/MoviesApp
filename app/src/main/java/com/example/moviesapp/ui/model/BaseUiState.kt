package com.example.moviesapp.ui.model

open class BaseUiState(
    open var isLoading: Boolean = false,
    open var errorMessage: String? = null
)