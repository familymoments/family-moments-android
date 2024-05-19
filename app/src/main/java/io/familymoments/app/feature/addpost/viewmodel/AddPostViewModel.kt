package io.familymoments.app.feature.addpost.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.core.network.util.createImageMultiPart
import io.familymoments.app.core.util.FileUtil
import io.familymoments.app.core.util.POST_PHOTO_MAX_SIZE
import io.familymoments.app.feature.addpost.AddPostMode
import io.familymoments.app.feature.addpost.uistate.AddPostUiState
import io.familymoments.app.feature.addpost.uistate.ExistPostUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val mode: Int = savedStateHandle[Route.EditPost.modeArg] ?: AddPostMode.ADD.mode
    private val editPostId: Long = savedStateHandle[Route.EditPost.editPostIdArg] ?: 0
    private val editImages: Array<String> = savedStateHandle[Route.EditPost.editImagesArg] ?: arrayOf()
    private val editContent: String = savedStateHandle[Route.EditPost.editContentArg] ?: ""

    private val _uiState = MutableStateFlow(AddPostUiState())
    val uiState = _uiState.asStateFlow()

    val filesState = mutableStateListOf<File>()

    init {
        initUiState()
        // 이미 업로드된 이미지 파일로 변환 (수정 화면 시)
        getFileList()
    }

    private fun initUiState() {
        _uiState.update {
            it.copy(
                mode = when (mode) {
                    AddPostMode.ADD.mode -> AddPostMode.ADD
                    else -> AddPostMode.EDIT
                },
                existPostUiState = ExistPostUiState(
                    editPostId = editPostId,
                    editImages = getEditImagesUrlList(this.editImages),
                    editContent = editContent
                )
            )
        }
    }

    private fun getFileList() {
        viewModelScope.launch(Dispatchers.IO) {
            getEditImagesUrlList(editImages).forEachIndexed { i, it ->
                val uri = Uri.parse(it)
                val file = FileUtil.uriToFile(uri, i)
                filesState.add(file)
            }
        }
    }

    private fun getEditImagesUrlList(editImages: Array<String>): List<String> {
        val regex = Regex("[\\[\\] ]")  // 문자열에서 공백, 대괄호 제거
        return editImages.getOrNull(0)?.replace(regex, "")?.split(",") ?: listOf()
    }

    suspend fun addPost(content: String, files: List<File>) {

        for (file in files) {
            // 이미지 크기 Log 출력
            val size = file.length() / 1024
            Timber.tag("Image").i("Image Size: $size KB")
        }

        val imagesMultipart = files.map { file ->
            createImageMultiPart(file, "imgs")
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

    fun editPost(index: Long, content: String, files: List<File>) {
        async(
            operation = {
                val imagesMultipart = files.map { file ->
                    createImageMultiPart(file, "imgs")
                }
                postRepository.editPost(index, content, imagesMultipart)
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

    fun addImages(uris: List<Uri>, context: Context, errorMessage: String) {
        if (uris.size + filesState.size <= POST_PHOTO_MAX_SIZE) {
            filesState.addAll(FileUtil.imageFilesResize(context, uris))
        } else {
            val availableCount = POST_PHOTO_MAX_SIZE - filesState.size
            filesState.addAll(FileUtil.imageFilesResize(context, uris.subList(0, availableCount)))
            _uiState.update {
                it.copy(
                    isSuccess = false,
                    errorMessage = errorMessage
                )
            }
        }
    }

    fun initSuccessState() {
        _uiState.value = _uiState.value.copy(
            isSuccess = null,
        )
    }
}
