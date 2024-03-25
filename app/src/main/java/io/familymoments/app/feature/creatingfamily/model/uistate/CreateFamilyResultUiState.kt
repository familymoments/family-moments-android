package io.familymoments.app.feature.creatingfamily.model.uistate

import io.familymoments.app.feature.creatingfamily.model.CreateFamilyResult

data class CreateFamilyResultUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = "",
    val result: CreateFamilyResult = CreateFamilyResult()
)
