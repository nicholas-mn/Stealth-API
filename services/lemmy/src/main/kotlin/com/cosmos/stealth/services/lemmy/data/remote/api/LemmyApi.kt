package com.cosmos.stealth.services.lemmy.data.remote.api

import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.Query
import com.cosmos.stealth.core.network.data.annotation.Url
import com.cosmos.stealth.services.lemmy.data.model.CommentSortType
import com.cosmos.stealth.services.lemmy.data.model.GetCommentsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetCommunityResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPersonDetailsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostsResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchType
import com.cosmos.stealth.services.lemmy.data.model.SortType

@Suppress("LongParameterList")
interface LemmyApi {

    @GET("/api/v3/post/list")
    suspend fun getPosts(
        @Url instance: String,
        @Query("community_name") communityName: String,
        @Query("sort") sort: SortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetPostsResponse

    @GET("/api/v3/community")
    suspend fun getCommunity(
        @Url instance: String,
        @Query("name") name: String,
        @Header("Forwarded") host: String? = null
    ): GetCommunityResponse

    @GET("/api/v3/post")
    suspend fun getPost(
        @Url instance: String,
        @Query("id") id: Int,
        @Header("Forwarded") host: String? = null
    ): GetPostResponse

    @GET("/api/v3/user")
    suspend fun getUser(
        @Url instance: String,
        @Query("username") username: String,
        @Query("sort") sort: SortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetPersonDetailsResponse

    @GET("/api/v3/comment/list")
    suspend fun getComments(
        @Url instance: String,
        @Query("post_id") postId: Int?,
        @Query("parent_id") parentId: Int?,
        @Query("sort") sort: CommentSortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetCommentsResponse

    @GET("/api/v3/search")
    suspend fun search(
        @Url instance: String,
        @Query("q") query: String,
        @Query("type_") type: SearchType,
        @Query("community_name") communityName: String? = null,
        @Query("sort") sort: SortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): SearchResponse

    companion object {
        const val MAX_DEPTH = 6
    }
}
