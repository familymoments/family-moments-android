package io.familymoments.app.feature.addpost.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.core.network.util.createImageMultiPart
import io.familymoments.app.feature.addpost.model.AddPostMode
import io.familymoments.app.feature.addpost.model.AddPostUiState
import io.familymoments.app.feature.addpost.model.ExistPostUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val mode: Int = savedStateHandle[Route.AddPost.modeArg] ?: AddPostMode.ADD.mode
    private val editPostId: Int = savedStateHandle[Route.AddPost.editPostIdArg] ?: 0
    private val editImages: Array<String> = savedStateHandle[Route.AddPost.editImagesArg] ?: arrayOf()
    private val editContent: String = savedStateHandle[Route.AddPost.editContentArg] ?: ""

    private val _uiState = MutableStateFlow(
        AddPostUiState(
            mode = when (mode) {
                AddPostMode.ADD.mode -> AddPostMode.ADD
                else -> AddPostMode.EDIT
            },
            existPostUiState = ExistPostUiState(
                editPostId = editPostId,
                editImages = editImages.toList(),
                editContent = editContent
            )
        )
    )
    val uiState = _uiState.asStateFlow()

    suspend fun addPost(content: String, files: List<File>) {
        val imagesMultipart = files.mapIndexed { index, file ->
            createImageMultiPart(file, "img${index + 1}")
        }
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                postRepository.addPost(familyId, content, imagesMultipart)
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value
                )
            },
            onFailure = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message
                )
            }
        )
    }
}
