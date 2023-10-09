package com.cosmos.stealth.services.teddit.data.remote.api

import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.Path
import com.cosmos.stealth.core.network.data.annotation.Query
import com.cosmos.stealth.core.network.data.annotation.Url
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.teddit.data.model.TedditUser
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_POST
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_SEARCH_SR
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_SEARCH_SUBREDDIT
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_SUBREDDIT
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_SUBREDDIT_ABOUT
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_USER_COMMENTS
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi.Endpoint.GET_USER_SUBMITTED

@Suppress("LongParameterList")
internal interface TedditApi {

    object Endpoint {
        const val GET_SUBREDDIT = "/r/{subreddit}/{sort}?api"
        const val GET_SUBREDDIT_ABOUT = "/r/{subreddit}/about?api"

        const val GET_SEARCH_SUBREDDIT = "/r/{subreddit}/search?api&restrict_sr=on&nsfw=on"

        const val GET_POST = "/{permalink}?api"

        const val GET_USER_SUBMITTED = "/u/{user}/submitted?api"
        const val GET_USER_COMMENTS = "/u/{user}/comments?api"

        const val GET_SEARCH_SR = "/subreddits/search?api&nsfw=on"
    }

    //region Subreddit

    @GET(GET_SUBREDDIT)
    suspend fun getSubreddit(
        @Url instance: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET(GET_SUBREDDIT_ABOUT)
    suspend fun getSubredditInfo(
        @Url instance: String,
        @Path("subreddit") subreddit: String,
        @Header("Forwarded") host: String? = null
    ): Child

    @GET(GET_SEARCH_SUBREDDIT)
    suspend fun searchInSubreddit(
        @Url instance: String,
        @Path("subreddit") subreddit: String,
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    @GET(GET_POST)
    suspend fun getPost(
        @Url instance: String,
        @Path("permalink", encoded = true) permalink: String,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: Sort,
        @Header("Forwarded") host: String? = null
    ): List<Listing>

    //region User

    @GET(GET_USER_SUBMITTED)
    suspend fun getUserPosts(
        @Url instance: String,
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): TedditUser

    @GET(GET_USER_COMMENTS)
    suspend fun getUserComments(
        @Url instance: String,
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): TedditUser

    //endregion

    //region Search

    @GET(GET_SEARCH_SR)
    suspend fun searchSubreddit(
        @Url instance: String,
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    companion object {
        const val BASE_URL = "https://teddit.net/"
    }
}
