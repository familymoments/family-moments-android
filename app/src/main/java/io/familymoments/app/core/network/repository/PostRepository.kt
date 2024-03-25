package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.feature.addpost.model.AddPostResponse
import io.familymoments.app.feature.album.model.GetAlbumDetailResponse
import io.familymoments.app.feature.album.model.GetAlbumResponse
import io.familymoments.app.feature.calendar.model.GetPostsByMonthResponse
import io.familymoments.app.feature.home.model.GetPostsResponse
import io.familymoments.app.feature.postdetail.model.response.DeletePostLovesResponse
import io.familymoments.app.feature.postdetail.model.response.DeletePostResponse
import io.familymoments.app.feature.postdetail.model.response.GetPostDetailResponse
import io.familymoments.app.feature.postdetail.model.response.GetPostLovesResponse
import io.familymoments.app.feature.postdetail.model.response.PostPostLovesResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PostRepository {
    suspend fun getPosts(familyId: Long): Flow<Resource<GetPostsResponse>>
    suspend fun loadMorePosts(familyId: Long, postId: Long): Flow<Resource<GetPostsResponse>>

    suspend fun getAlbum(familyId: Long): Flow<Resource<GetAlbumResponse>>
    suspend fun loadMoreAlbum(familyId: Long, postId: Long): Flow<Resource<GetAlbumResponse>>
    suspend fun getAlbumDetail(postId: Long): Flow<Resource<GetAlbumDetailResponse>>

    suspend fun getPostsByMonth(familyId: Long, year: Int, month: Int): Flow<Resource<GetPostsByMonthResponse>>
    suspend fun getPostsByDay(familyId: Long, year: Int, month: Int, day: Int): Flow<Resource<GetPostsResponse>>
    suspend fun loadMorePostsByDay(
        familyId: Long,
        year: Int,
        month: Int,
        day: Int,
        postId: Long
    ): Flow<Resource<GetPostsResponse>>

    suspend fun addPost(
        familyId: Long,
        content: String,
        uriList: List<MultipartBody.Part>?
    ): Flow<Resource<AddPostResponse>>
    suspend fun getPostDetail(index:Long):Flow<Resource<GetPostDetailResponse>>
    suspend fun getPostLoves(index:Long):Flow<Resource<GetPostLovesResponse>>
    suspend fun postPostLoves(postId:Long):Flow<Resource<PostPostLovesResponse>>
    suspend fun deletePostLoves(postId:Long):Flow<Resource<DeletePostLovesResponse>>
    suspend fun deletePost(index:Long):Flow<Resource<DeletePostResponse>>
}
