package io.familymoments.app.feature.addpost.model

import androidx.compose.runtime.Immutable

@Immutable
data class AddPostResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: PostResult = PostResult()
)

@Immutable
data class PostResult(
    val postId: Long = 0,
    val writer: String = "",
    val profileImg: String = "",
    val content: String = "",
    val imgs: List<String> = emptyList(),
    val createdAt: String = "",
    val countLove: Int = 0,
    val loved: Boolean = false
)
