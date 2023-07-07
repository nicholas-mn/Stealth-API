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

@Suppress("LongParameterList")
interface TedditApi {

    //region Subreddit

    @GET("/r/{subreddit}/{sort}?api")
    suspend fun getSubreddit(
        @Url instance: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET("/r/{subreddit}/about?api")
    suspend fun getSubredditInfo(
        @Url instance: String,
        @Path("subreddit") subreddit: String,
        @Header("Forwarded") host: String? = null
    ): Child

    @GET("/r/{subreddit}/search?api&restrict_sr=on&nsfw=on")
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

    @GET("{permalink}?api")
    suspend fun getPost(
        @Url instance: String,
        @Path("permalink", encoded = true) permalink: String,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: Sort,
        @Header("Forwarded") host: String? = null
    ): List<Listing>

    //region User

    @GET("/u/{user}/submitted?api")
    suspend fun getUserPosts(
        @Url instance: String,
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Header("Forwarded") host: String? = null
    ): TedditUser

    @GET("/u/{user}/comments?api")
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

    @GET("/subreddits/search?api&nsfw=on")
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
