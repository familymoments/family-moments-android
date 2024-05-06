package io.familymoments.app.feature.calendar.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.feature.calendar.uistate.CalendarDayUiState
import io.familymoments.app.feature.home.uistate.PostPopupType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarDayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val initialLocalDate = LocalDate.parse(savedStateHandle.get<String>(Route.CalendarDay.localDateStringArgs))

    private val _calendarDayUiState = MutableStateFlow(CalendarDayUiState(selectedDate = initialLocalDate))
    val calendarDayUiState = _calendarDayUiState.asStateFlow()

    private var minPostId: Long = 0

    init {
        getPostsByDay()
        getNickname()
    }

    private fun getNickname() {
        viewModelScope.launch {
            val nickname = userInfoPreferencesDataSource.loadUserProfile().nickName
            _calendarDayUiState.value = _calendarDayUiState.value.copy(
                userNickname = nickname
            )
        }
    }

    private fun getPostsByDay() {
        Timber.d("getPostsByDay")
        val selectedDate = _calendarDayUiState.value.selectedDate
        val year = selectedDate.year
        val month = selectedDate.monthValue
        val day = selectedDate.dayOfMonth
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                postRepository.getPostsByDay(familyId, year, month, day)
            },
            onSuccess = {
                _calendarDayUiState.value = _calendarDayUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    posts = it.result
                )
                minPostId = it.result.minOf { post -> post.postId }
            },
            onFailure = {
                _calendarDayUiState.value = _calendarDayUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message,
                    posts = emptyList()
                )
            }
        )
    }

    fun loadMorePostsByDay() {
        Timber.d("loadMorePostsByDay")
        val selectedDate = _calendarDayUiState.value.selectedDate
        val year = selectedDate.year
        val month = selectedDate.monthValue
        val day = selectedDate.dayOfMonth
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                postRepository.loadMorePostsByDay(familyId, year, month, day, minPostId)
            },
            onSuccess = {
                _calendarDayUiState.value = _calendarDayUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    posts = _calendarDayUiState.value.posts + it.result
                )
                minPostId = it.result.minOf { post -> post.postId }
            },
            onFailure = {
                _calendarDayUiState.value = _calendarDayUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message
                )
            }
        )
    }

    fun getPostsByPrevDay() {
        Timber.d("getPostsByPrevDay")
        val selectedDate = _calendarDayUiState.value.selectedDate.minusDays(1)
        _calendarDayUiState.value = _calendarDayUiState.value.copy(selectedDate = selectedDate)
        getPostsByDay()
    }

    fun getPostsByNextDay() {
        Timber.d("getPostsByNextDay")
        val selectedDate = _calendarDayUiState.value.selectedDate.plusDays(1)
        _calendarDayUiState.value = _calendarDayUiState.value.copy(selectedDate = selectedDate)
        getPostsByDay()
    }

    fun deletePost(postId: Long) {
        Timber.d("deletePost")
        async(
            operation = { postRepository.deletePost(postId) },
            onSuccess = { response ->
                Timber.d("deletePost onSuccess: $response")
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostSuccess
                    )
                }
                getPostsByDay()
            },
            onFailure = { t ->
                Timber.d("deletePost onFailure: ${t.message}")
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostFailure
                    )
                }
            }
        )
    }

    fun deletePostLoves(postId: Long) {
        Timber.d("deletePostLoves")
        async(
            operation = { postRepository.deletePostLoves(postId) },
            onSuccess = { response ->
                Timber.d("deletePostLoves onSuccess: $response")
                _calendarDayUiState.update {
                    it.copy(
                        posts = it.posts.map { post ->
                            if (post.postId == postId) {
                                post.copy(loved = false)
                            } else {
                                post
                            }
                        },
                    )
                }
            },
            onFailure = { t ->
                Timber.d("deletePostLoves onFailure: ${t.message}")
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeleteLovesFailure
                    )
                }
            }
        )
    }

    fun postPostLoves(postId: Long) {
        Timber.d("postPostLoves")
        async(
            operation = { postRepository.postPostLoves(postId) },
            onSuccess = { response ->
                Timber.d("postPostLoves onSuccess: $response")
                _calendarDayUiState.update {
                    it.copy(
                        posts = it.posts.map { post ->
                            if (post.postId == postId) {
                                post.copy(loved = true)
                            } else {
                                post
                            }
                        },
                    )
                }
            },
            onFailure = { t ->
                Timber.d("postPostLoves onFailure: ${t.message}")
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.PostLovesFailure
                    )
                }
            }
        )
    }

    fun showDeletePostPopup(postId: Long) {
        Timber.d("showDeletePostPopup")
        _calendarDayUiState.update {
            it.copy(popup = PostPopupType.DeletePost(postId))
        }
    }

    fun showReportPostPopup(postId: Long) {
        Timber.d("showReportPostPopup")
        _calendarDayUiState.update {
            it.copy(popup = PostPopupType.ReportPost(postId))
        }
    }

    fun dismissPopup() {
        _calendarDayUiState.update {
            it.copy(popup = null)
        }
    }
}
