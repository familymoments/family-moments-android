package io.familymoments.app.feature.addpost.model

import androidx.compose.runtime.Immutable

@Immutable
data class AddPostUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = null,
    val mode:AddPostMode = AddPostMode.ADD,
    val existPostUiState: ExistPostUiState = ExistPostUiState()
)

data class ExistPostUiState(
    val editPostId: Int = 0,
    val editImages: List<String> = listOf(),
    val editContent: String = ""
)
