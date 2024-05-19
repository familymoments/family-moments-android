package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.PostService
import io.familymoments.app.core.network.dto.request.AddPostRequest
import io.familymoments.app.core.network.dto.response.AddPostResponse
import io.familymoments.app.core.network.dto.response.DeletePostLovesResponse
import io.familymoments.app.core.network.dto.response.DeletePostResponse
import io.familymoments.app.core.network.dto.response.GetAlbumDetailResponse
import io.familymoments.app.core.network.dto.response.GetAlbumResponse
import io.familymoments.app.core.network.dto.response.GetPostDetailResponse
import io.familymoments.app.core.network.dto.response.GetPostLovesResponse
import io.familymoments.app.core.network.dto.response.GetPostsByMonthResponse
import io.familymoments.app.core.network.dto.response.GetPostsResponse
import io.familymoments.app.core.network.dto.response.PostPostLovesResponse
import io.familymoments.app.core.network.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService
) : PostRepository {
    override suspend fun getPosts(familyId: Long): Flow<Resource<GetPostsResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPosts(familyId)
            val responseBody = response.body() ?: GetPostsResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun loadMorePosts(
        familyId: Long,
        postId: Long
    ): Flow<Resource<GetPostsResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.loadMorePosts(familyId, postId)
            val responseBody = response.body() ?: GetPostsResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                // 새로운 Post가 없으면 404 : post가 존재하지 않습니다 에러 발생
                // 새로운 Post가 없으면 더 이상 로드하지 않으면 되니까, 굳이 flow를 emit하여 UiState를 업데이트할 필요가 없음
                if (responseBody.code != 404) {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getAlbum(familyId: Long): Flow<Resource<GetAlbumResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getAlbum(familyId)
            val responseBody = response.body() ?: GetAlbumResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun loadMoreAlbum(
        familyId: Long,
        postId: Long
    ): Flow<Resource<GetAlbumResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.loadMoreAlbum(familyId, postId)
            val responseBody = response.body() ?: GetAlbumResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                // 새로운 Post가 없으면 404 : post가 존재하지 않습니다 에러 발생
                // 새로운 Post가 없으면 더 이상 로드하지 않으면 되니까, 굳이 flow를 emit하여 UiState를 업데이트할 필요가 없음
                if (responseBody.code != 404) {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getAlbumDetail(postId: Long): Flow<Resource<GetAlbumDetailResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getAlbumDetail(postId)
            val responseBody =
                response.body() ?: GetAlbumDetailResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getPostsByMonth(
        familyId: Long,
        year: Int,
        month: Int
    ): Flow<Resource<GetPostsByMonthResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostsByMonth(familyId, year, month)
            val responseBody =
                response.body() ?: GetPostsByMonthResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getPostsByDay(
        familyId: Long,
        year: Int,
        month: Int,
        day: Int
    ): Flow<Resource<GetPostsResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostsByDay(familyId, year, month, day)
            val responseBody = response.body() ?: GetPostsResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun loadMorePostsByDay(
        familyId: Long,
        year: Int,
        month: Int,
        day: Int,
        postId: Long
    ): Flow<Resource<GetPostsResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.loadMorePostsByDay(familyId, year, month, day, postId)
            val responseBody = response.body() ?: GetPostsResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                // 새로운 Post가 없으면 404 : post가 존재하지 않습니다 에러 발생
                // 새로운 Post가 없으면 더 이상 로드하지 않으면 되니까, 굳이 flow를 emit하여 UiState를 업데이트할 필요가 없음
                if (responseBody.code != 404) {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun addPost(
        familyId: Long,
        content: String,
        multipartImgs: List<MultipartBody.Part>?
    ): Flow<Resource<AddPostResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.addPost(familyId, AddPostRequest(content), multipartImgs)
            val responseBody = response.body() ?: AddPostResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getPostDetail(index: Long): Flow<Resource<GetPostDetailResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostDetail(index)
            val responseBody = response.body() ?: GetPostDetailResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getPostLoves(index: Long): Flow<Resource<GetPostLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostLoves(index)
            val responseBody = response.body() ?: GetPostLovesResponse()
            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun postPostLoves(postId: Long): Flow<Resource<PostPostLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.postPostloves(
                io.familymoments.app.core.network.dto.request.PostLovesRequest(
                    postId
                )
            )
            val responseBody = response.body() ?: PostPostLovesResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun deletePostLoves(postId: Long): Flow<Resource<DeletePostLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.deletePostloves(
                io.familymoments.app.core.network.dto.request.PostLovesRequest(
                    postId
                )
            )
            val responseBody =
                response.body() ?: DeletePostLovesResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun deletePost(index: Long): Flow<Resource<DeletePostResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.deletePost(index)
            val responseBody = response.body() ?: DeletePostResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun editPost(
        index: Long,
        content: String,
        multipartImgs: List<MultipartBody.Part>?
        ): Flow<Resource<AddPostResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.editPost(index, AddPostRequest(content), multipartImgs)
            val responseBody = response.body() ?: AddPostResponse()

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
