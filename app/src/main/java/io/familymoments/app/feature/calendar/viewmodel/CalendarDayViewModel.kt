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
                if (it.result.isNotEmpty()) {
                    minPostId = it.result.minOf { post -> post.postId }
                }
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
                if (it.result.isNotEmpty()) {
                    minPostId = it.result.minOf { post -> post.postId }
                }
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
        val selectedDate = _calendarDayUiState.value.selectedDate.minusDays(1)
        _calendarDayUiState.value = _calendarDayUiState.value.copy(selectedDate = selectedDate)
        getPostsByDay()
    }

    fun getPostsByNextDay() {
        val selectedDate = _calendarDayUiState.value.selectedDate.plusDays(1)
        _calendarDayUiState.value = _calendarDayUiState.value.copy(selectedDate = selectedDate)
        getPostsByDay()
    }

    fun deletePost(postId: Long) {
        async(
            operation = { postRepository.deletePost(postId) },
            onSuccess = {
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostSuccess
                    )
                }
                getPostsByDay()
            },
            onFailure = {
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostFailure
                    )
                }
            }
        )
    }

    fun deletePostLoves(postId: Long) {
        async(
            operation = { postRepository.deletePostLoves(postId) },
            onSuccess = {
                _calendarDayUiState.update {
                    it.copy(
                        posts = it.posts.map { post ->
                            if (post.postId == postId) {
                                post.copy(
                                    loved = false,
                                    countLove = post.countLove - 1
                                )
                            } else {
                                post
                            }
                        },
                    )
                }
            },
            onFailure = {
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.DeleteLovesFailure
                    )
                }
            }
        )
    }

    fun postPostLoves(postId: Long) {
        async(
            operation = { postRepository.postPostLoves(postId) },
            onSuccess = {
                _calendarDayUiState.update {
                    it.copy(
                        posts = it.posts.map { post ->
                            if (post.postId == postId) {
                                post.copy(
                                    loved = true,
                                    countLove = post.countLove + 1
                                )
                            } else {
                                post
                            }
                        },
                    )
                }
            },
            onFailure = {
                _calendarDayUiState.update {
                    it.copy(
                        popup = PostPopupType.PostLovesFailure
                    )
                }
            }
        )
    }

    fun showDeletePostPopup(postId: Long) {
        _calendarDayUiState.update {
            it.copy(popup = PostPopupType.DeletePost(postId))
        }
    }

    fun showReportPostPopup(postId: Long) {
        _calendarDayUiState.update {
            it.copy(popup = PostPopupType.ReportPost(postId))
        }
    }

    fun dismissPopup() {
        _calendarDayUiState.update {
            it.copy(popup = null)
        }
    }

    fun reportPost(postId: Long, reason: String, details: String) {
        async(
            operation = {
                postRepository.reportPost(postId, reason, details)
            },
            onSuccess = {
                _calendarDayUiState.update {
                    it.copy(
                        isSuccess = true,
                        popup = PostPopupType.ReportPostSuccess
                    )
                }
            },
            onFailure = { e ->
                _calendarDayUiState.update {
                    it.copy(
                        isSuccess = false,
                        popup = PostPopupType.ReportPostFailure(e.message.toString())
                    )
                }
            }
        )
    }
}
