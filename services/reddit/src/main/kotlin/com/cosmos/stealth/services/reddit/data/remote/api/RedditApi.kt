package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.data.annotation.Body
import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.POST
import com.cosmos.stealth.core.network.data.annotation.Path
import com.cosmos.stealth.core.network.data.annotation.Query
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.reddit.data.model.Token
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_MORE_CHILDREN
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_POST
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SEARCH_LINK
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SEARCH_SR
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SEARCH_SUBREDDIT
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SEARCH_USER
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SUBREDDIT
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_SUBREDDIT_ABOUT
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_USER_ABOUT
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_USER_COMMENTS
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.GET_USER_SUBMITTED
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.POST_ACCESS_TOKEN
import io.ktor.http.Parameters

@Suppress("TooManyFunctions", "LongParameterList")
internal interface RedditApi {

    object Endpoint {
        const val GET_SUBREDDIT = "/r/{subreddit}/{sort}"
        const val GET_SUBREDDIT_ABOUT = "/r/{subreddit}/about"

        const val GET_SEARCH_SUBREDDIT = "/r/{subreddit}/search?restrict_sr=1&include_over_18=1"

        const val GET_POST = "/comments/{permalink}"

        const val GET_MORE_CHILDREN = "/api/morechildren?api_type=json"

        const val GET_USER_ABOUT = "/user/{user}/about"
        const val GET_USER_SUBMITTED = "/user/{user}/submitted/"
        const val GET_USER_COMMENTS = "/user/{user}/comments/"

        const val GET_SEARCH_LINK = "/search?type=link&include_over_18=1"
        const val GET_SEARCH_USER = "/search?type=user&include_over_18=1"
        const val GET_SEARCH_SR = "/search?type=sr&include_over_18=1"

        const val POST_ACCESS_TOKEN = "/api/v1/access_token"
    }

    //region Subreddit

    @GET(GET_SUBREDDIT)
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("geo_filter") geoFilter: String? = "GLOBAL",
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET(GET_SUBREDDIT_ABOUT)
    suspend fun getSubredditInfo(
        @Path("subreddit") subreddit: String,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Child

    @GET(GET_SEARCH_SUBREDDIT)
    suspend fun searchInSubreddit(
        @Path("subreddit") subreddit: String,
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    @GET(GET_POST)
    suspend fun getPost(
        @Path("permalink", encoded = true) permalink: String,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: Sort,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): List<Listing>

    @GET(GET_MORE_CHILDREN)
    suspend fun getMoreChildren(
        @Query("children") children: String,
        @Query("link_id") linkId: String,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null,
        parentId: String? = null,
        refLink: String? = null,
        depth: Int? = null
    ): MoreChildren

    //region User

    @GET(GET_USER_ABOUT)
    suspend fun getUserInfo(
        @Path("user") user: String,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Child

    @GET(GET_USER_SUBMITTED)
    suspend fun getUserPosts(
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET(GET_USER_COMMENTS)
    suspend fun getUserComments(
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    //region Search

    @GET(GET_SEARCH_LINK)
    suspend fun searchPost(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET(GET_SEARCH_USER)
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET(GET_SEARCH_SR)
    suspend fun searchSubreddit(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Authorization") bearer: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    @POST(POST_ACCESS_TOKEN)
    suspend fun getAccessToken(
        @Body parameters: Parameters,
        @Header("Authorization") bearer: String
    ): Token

    companion object {
        const val BASE_URL = "https://www.reddit.com/"
        const val BASE_URL_OAUTH = "https://oauth.reddit.com/"
        const val BASE_URL_OLD = "https://old.reddit.com/"
    }
}
