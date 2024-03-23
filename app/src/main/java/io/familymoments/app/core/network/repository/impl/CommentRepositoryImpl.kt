package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.CommentService
import io.familymoments.app.core.network.repository.CommentRepository
import io.familymoments.app.feature.postdetail.model.response.GetCommentsByPostIndexResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {
    override suspend fun getCommentsByPostIndex(index: Int): Flow<Resource<GetCommentsByPostIndexResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = commentService.getCommentsByPostIndex(index)
            val responseBody = response.body() ?: GetCommentsByPostIndexResponse()

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
