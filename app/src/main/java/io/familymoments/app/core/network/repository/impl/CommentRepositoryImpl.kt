package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.CommentService
import io.familymoments.app.core.network.repository.CommentRepository
import io.familymoments.app.feature.postdetail.model.request.CommentLovesRequest
import io.familymoments.app.feature.postdetail.model.request.PostCommentRequest
import io.familymoments.app.feature.postdetail.model.response.DeleteCommentLovesResponse
import io.familymoments.app.feature.postdetail.model.response.PostCommentLovesResponse
import io.familymoments.app.feature.postdetail.model.response.DeleteCommentResponse
import io.familymoments.app.feature.postdetail.model.response.GetCommentsIndexResponse
import io.familymoments.app.feature.postdetail.model.response.PostCommentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {
    override suspend fun getPostComments(index: Long): Flow<Resource<GetCommentsIndexResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = commentService.getPostComments(index)
            val responseBody = response.body() ?: GetCommentsIndexResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun postComment(comment: String, index: Long): Flow<Resource<PostCommentResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = commentService.postComment(index, PostCommentRequest(comment))
            val responseBody = response.body() ?: PostCommentResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun deleteComment(index: Long): Flow<Resource<DeleteCommentResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = commentService.deleteComment(index)
            val responseBody = response.body() ?: DeleteCommentResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun postCommentLoves(commentId: Long): Flow<Resource<PostCommentLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = commentService.postCommentLoves(CommentLovesRequest(commentId))
            val responseBody = response.body() ?: PostCommentLovesResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun deleteCommentLoves(commentId: Long): Flow<Resource<DeleteCommentLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            Timber.tag("hkhk").d("삭제 수행")
            val response = commentService.deleteCommentLoves(CommentLovesRequest(commentId))
            val responseBody = response.body() ?: DeleteCommentLovesResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }
}
