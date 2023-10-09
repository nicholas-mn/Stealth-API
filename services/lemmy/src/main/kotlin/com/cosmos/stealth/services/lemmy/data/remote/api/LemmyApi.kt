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
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_COMMENT_LIST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_COMMUNITY_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_POST_LIST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_POST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_USER_V3

@Suppress("LongParameterList")
internal interface LemmyApi {

    object Endpoint {
        const val GET_POST_LIST_V3 = "/api/v3/post/list"

        const val GET_COMMUNITY_V3 = "/api/v3/community"

        const val GET_POST_V3 = "/api/v3/post"

        const val GET_USER_V3 = "/api/v3/user"

        const val GET_COMMENT_LIST_V3 = "/api/v3/comment/list"

        const val GET_SEARCH_V3 = "/api/v3/search"
    }

    @GET(GET_POST_LIST_V3)
    suspend fun getPosts(
        @Url instance: String,
        @Query("community_name") communityName: String?,
        @Query("sort") sort: SortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetPostsResponse

    @GET(GET_COMMUNITY_V3)
    suspend fun getCommunity(
        @Url instance: String,
        @Query("name") name: String,
        @Header("Forwarded") host: String? = null
    ): GetCommunityResponse

    @GET(GET_POST_V3)
    suspend fun getPost(
        @Url instance: String,
        @Query("id") id: Int,
        @Header("Forwarded") host: String? = null
    ): GetPostResponse

    @GET(GET_USER_V3)
    suspend fun getUser(
        @Url instance: String,
        @Query("username") username: String,
        @Query("sort") sort: SortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetPersonDetailsResponse

    @GET(GET_COMMENT_LIST_V3)
    suspend fun getComments(
        @Url instance: String,
        @Query("post_id") postId: Int?,
        @Query("parent_id") parentId: Int?,
        @Query("sort") sort: CommentSortType?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): GetCommentsResponse

    @GET(Endpoint.GET_SEARCH_V3)
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
