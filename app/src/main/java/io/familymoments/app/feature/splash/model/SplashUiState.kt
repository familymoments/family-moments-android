package io.familymoments.app.feature.splash.model

data class SplashUiState(
    val isLoading: Boolean? = null,
    val isSuccess: Boolean? = null,
    val error: Throwable? = null
)
