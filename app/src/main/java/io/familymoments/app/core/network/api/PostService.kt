package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.dto.request.AddPostRequest
import io.familymoments.app.core.network.dto.request.PostLovesRequest
import io.familymoments.app.core.network.dto.request.ReportRequest
import io.familymoments.app.core.network.dto.response.AddPostResponse
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.DeletePostLovesResponse
import io.familymoments.app.core.network.dto.response.DeletePostResponse
import io.familymoments.app.core.network.dto.response.GetAlbumDetailResponse
import io.familymoments.app.core.network.dto.response.GetAlbumResponse
import io.familymoments.app.core.network.dto.response.GetPostDetailResponse
import io.familymoments.app.core.network.dto.response.GetPostLovesResponse
import io.familymoments.app.core.network.dto.response.GetPostsByMonthResponse
import io.familymoments.app.core.network.dto.response.GetPostsResponse
import io.familymoments.app.core.network.dto.response.PostPostLovesResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {
    @GET("/posts")
    suspend fun getPosts(@Query("familyId") familyId: Long): Response<GetPostsResponse>

    @GET("/posts")
    suspend fun loadMorePosts(
        @Query("familyId") familyId: Long,
        @Query("postId") postId: Long
    ): Response<GetPostsResponse>

    @GET("/posts/album")
    suspend fun getAlbum(@Query("familyId") familyId: Long): Response<GetAlbumResponse>

    @GET("/posts/album")
    suspend fun loadMoreAlbum(
        @Query("familyId") familyId: Long,
        @Query("postId") postId: Long
    ): Response<GetAlbumResponse>

    @GET("/posts/album/{postId}")
    suspend fun getAlbumDetail(@Path("postId") postId: Long): Response<GetAlbumDetailResponse>

    @GET("/posts/calendar")
    suspend fun getPostsByMonth(
        @Query("familyId") familyId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<GetPostsByMonthResponse>

    @GET("/posts/calendar")
    suspend fun getPostsByDay(
        @Query("familyId") familyId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Response<GetPostsResponse>

    @GET("/posts/calendar")
    suspend fun loadMorePostsByDay(
        @Query("familyId") familyId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("postId") postId: Long
    ): Response<GetPostsResponse>

    @Multipart
    @POST("/posts")
    suspend fun addPost(
        @Query("familyId") familyId: Long,
        @Part("postInfo") postInfo: AddPostRequest,
        @Part images: List<MultipartBody.Part>?
    ): Response<AddPostResponse>

    @GET("/posts/{index}")
    suspend fun getPostDetail(@Path("index") index: Long): Response<GetPostDetailResponse>

    @GET("/posts/{index}/post-loves")
    suspend fun getPostLoves(@Path("index") index: Long): Response<GetPostLovesResponse>

    @POST("/postloves")
    suspend fun postPostloves(@Body postlovesRequest: PostLovesRequest): Response<PostPostLovesResponse>

    @HTTP(method = "DELETE", hasBody = true, path = "/postloves")
    suspend fun deletePostloves(@Body postlovesRequest: PostLovesRequest): Response<DeletePostLovesResponse>

    @DELETE("/posts/{index}")
    suspend fun deletePost(@Path("index") index: Long): Response<DeletePostResponse>

    @Multipart
    @POST("/posts/{index}/edit")
    suspend fun editPost(
        @Path("index") index:Long,
        @Part("postInfo") postInfo: AddPostRequest,
        @Part images: List<MultipartBody.Part>?
    ):Response<AddPostResponse>

    @POST("/posts/report/{postId}")
    suspend fun reportPost(
        @Path(value = "postId") postId: Long,
        @Body reportRequest: ReportRequest
    ): Response<ApiResponse<String>>
}
