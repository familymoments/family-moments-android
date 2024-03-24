package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.PostService
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.core.network.util.createPostInfoRequestBody
import io.familymoments.app.feature.addpost.model.AddPostResponse
import io.familymoments.app.feature.album.model.GetAlbumDetailResponse
import io.familymoments.app.feature.album.model.GetAlbumResponse
import io.familymoments.app.feature.calendar.model.GetPostsByMonthResponse
import io.familymoments.app.feature.home.model.GetPostsResponse
import io.familymoments.app.feature.postdetail.model.request.PostLovesRequest
import io.familymoments.app.feature.postdetail.model.response.DeletePostLovesResponse
import io.familymoments.app.feature.postdetail.model.response.GetPostByIndexResponse
import io.familymoments.app.feature.postdetail.model.response.GetPostLovesByIndexResponse
import io.familymoments.app.feature.postdetail.model.response.PostPostLovesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class PostRepositoryImpl(
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

    override suspend fun loadMorePosts(familyId: Long, postId: Long): Flow<Resource<GetPostsResponse>> {
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

    override suspend fun loadMoreAlbum(familyId: Long, postId: Long): Flow<Resource<GetAlbumResponse>> {
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
            val responseBody = response.body() ?: GetAlbumDetailResponse()

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
            val responseBody = response.body() ?: GetPostsByMonthResponse()

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
        imageFiles: List<MultipartBody.Part>?
    ): Flow<Resource<AddPostResponse>> {
        return flow {
            emit(Resource.Loading)
            val postInfo = createPostInfoRequestBody(content)

            val response = postService.addPost(familyId, postInfo, imageFiles)
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

    override suspend fun getPostByIndex(index: Int): Flow<Resource<GetPostByIndexResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostByIndex(index)
            val responseBody = response.body() ?: GetPostByIndexResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun getPostLovesByIndex(index: Int): Flow<Resource<GetPostLovesByIndexResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.getPostLovesByIndex(index)
            val responseBody = response.body() ?: GetPostLovesByIndexResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun postPostLoves(postId: Int): Flow<Resource<PostPostLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.postPostloves(PostLovesRequest(postId))
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

    override suspend fun deletePostLoves(postId: Int): Flow<Resource<DeletePostLovesResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = postService.deletePostloves(PostLovesRequest(postId))
            val responseBody = response.body() ?: DeletePostLovesResponse()

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
