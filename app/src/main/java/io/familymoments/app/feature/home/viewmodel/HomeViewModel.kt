package io.familymoments.app.feature.home.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.HttpResponseMessage.NO_POST_404
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.feature.home.uistate.HomeUiState
import io.familymoments.app.feature.home.uistate.PostPopupType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val familyRepository: FamilyRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private var minPostId: Long = 0

    fun getNicknameDday() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                familyRepository.getNicknameDday(familyId)
            },
            onSuccess = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    userNickname = it.result.nickname,
                    dday = it.result.dday
                )
            },
            onFailure = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message
                )
            }
        )
    }

    fun getPosts() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                postRepository.getPosts(familyId)
            },
            onSuccess = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    errorMessage = null,
                    posts = it.result
                )
                if (it.result.isNotEmpty()) {
                    minPostId = it.result.minOf { post -> post.postId }
                }
            },
            onFailure = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message,
                    posts = if (it.message == NO_POST_404) emptyList() else _homeUiState.value.posts
                )
            }
        )
    }

    fun loadMorePosts() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                postRepository.loadMorePosts(familyId, minPostId)
            },
            onSuccess = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    posts = _homeUiState.value.posts + it.result
                )
                if (it.result.isNotEmpty()) {
                    minPostId = it.result.minOf { post -> post.postId }
                }
            },
            onFailure = {
                _homeUiState.value = _homeUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message
                )
            }
        )
    }

    fun postPostLoves(postId: Long) {
        async(
            operation = { postRepository.postPostLoves(postId) },
            onSuccess = {
                _homeUiState.update {
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
                _homeUiState.update {
                    it.copy(
                        popup = PostPopupType.PostLovesFailure
                    )
                }
            }
        )
    }

    fun deletePostLoves(postId: Long) {
        async(
            operation = { postRepository.deletePostLoves(postId) },
            onSuccess = {
                _homeUiState.update {
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
                _homeUiState.update {
                    it.copy(
                        popup = PostPopupType.DeleteLovesFailure
                    )
                }
            }
        )
    }

    fun deletePost(postId: Long) {
        async(
            operation = { postRepository.deletePost(postId) },
            onSuccess = {
                _homeUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostSuccess
                    )
                }
                getPosts()
            },
            onFailure = {
                _homeUiState.update {
                    it.copy(
                        popup = PostPopupType.DeletePostFailure
                    )
                }
            }
        )
    }


    fun reportPost(postId: Long, reason: String, details: String) {
        async(
            operation = { postRepository.reportPost(postId, reason, details) },
            onSuccess = {
                _homeUiState.update {
                    it.copy(
                        isSuccess = true,
                        popup = PostPopupType.ReportPostSuccess
                    )
                }
            },
            onFailure = { e->
                _homeUiState.update {
                    it.copy(
                        isSuccess = false,
                        popup = PostPopupType.ReportPostFailure(e.message.toString())
                    )
                }
            }
        )
    }

    fun showDeletePostPopup(postId: Long) {
        _homeUiState.update {
            it.copy(popup = PostPopupType.DeletePost(postId))
        }
    }

    fun showReportPostPopup(postId: Long) {
        _homeUiState.update {
            it.copy(popup = PostPopupType.ReportPost(postId))
        }
    }

    /**
     * popup을 null로 초기화 해주면서 화면에서 팝업을 안 보이도록 처리
     */
    fun dismissPopup() {
        _homeUiState.update {
            it.copy(popup = null)
        }
    }
}
