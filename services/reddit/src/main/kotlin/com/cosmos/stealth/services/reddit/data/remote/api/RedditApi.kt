package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.Path
import com.cosmos.stealth.core.network.data.annotation.Query
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting

@Suppress("TooManyFunctions", "LongParameterList")
interface RedditApi {

    //region Subreddit

    @GET("/r/{subreddit}/{sort}")
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("geo_filter") geoFilter: String? = "GLOBAL",
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET("/r/{subreddit}/about")
    suspend fun getSubredditInfo(@Path("subreddit") subreddit: String, @Header("Forwarded") host: String? = null): Child

    @GET("/r/{subreddit}/search?restrict_sr=1&include_over_18=1")
    suspend fun searchInSubreddit(
        @Path("subreddit") subreddit: String,
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    @GET("{permalink}")
    suspend fun getPost(
        @Path("permalink", encoded = true) permalink: String,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: Sort,
        @Header("Forwarded") host: String? = null
    ): List<Listing>

    @GET("/api/morechildren?api_type=json")
    suspend fun getMoreChildren(
        @Query("children") children: String,
        @Query("link_id") linkId: String,
        @Header("Forwarded") host: String? = null
    ): MoreChildren

    //region User

    @GET("/user/{user}/about")
    suspend fun getUserInfo(@Path("user") user: String, @Header("Forwarded") host: String? = null): Child

    @GET("/user/{user}/submitted/")
    suspend fun getUserPosts(
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET("/user/{user}/comments/")
    suspend fun getUserComments(
        @Path("user") user: String,
        @Query("sort") sort: Sort,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    //region Search

    @GET("/search?type=link&include_over_18=1")
    suspend fun searchPost(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET("/search?type=user&include_over_18=1")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    @GET("/search?type=sr&include_over_18=1")
    suspend fun searchSubreddit(
        @Query("q") query: String,
        @Query("sort") sort: Sort?,
        @Query("t") timeSorting: TimeSorting?,
        @Query("after") after: String? = null,
        @Query("limit") limit: Int? = null,
        @Header("Forwarded") host: String? = null
    ): Listing

    //endregion

    companion object {
        const val BASE_URL = "https://www.reddit.com/"
        const val BASE_URL_OLD = "https://old.reddit.com/"
    }
}
