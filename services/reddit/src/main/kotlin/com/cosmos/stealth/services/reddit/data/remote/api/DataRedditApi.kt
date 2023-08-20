package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import io.ktor.client.HttpClient
import io.ktor.client.call.body

@Suppress("TooManyFunctions")
class DataRedditApi(client: HttpClient, urlSubstitutor: UrlSubstitutor) : BaseRedditApi(client, urlSubstitutor) {

    override suspend fun getSubreddit(
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        geoFilter: String?,
        host: String?
    ): Listing {
        return getRawSubreddit(subreddit, sort, timeSorting, after, limit, geoFilter, host).body()
    }

    override suspend fun getSubredditInfo(subreddit: String, host: String?): Child {
        return getRawSubredditInfo(subreddit, host).body()
    }

    override suspend fun searchInSubreddit(
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return searchInSubredditRaw(subreddit, query, sort, timeSorting, after, limit, host).body()
    }

    override suspend fun getPost(permalink: String, limit: Int?, sort: Sort, host: String?): List<Listing> {
        return getRawPost(permalink, limit, sort, host).body()
    }

    override suspend fun getMoreChildren(children: String, linkId: String, host: String?): MoreChildren {
        return getRawMoreChildren(children, linkId, host).body()
    }

    override suspend fun getUserInfo(user: String, host: String?): Child {
        return getRawUserInfo(user, host).body()
    }

    override suspend fun getUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return getRawUserPosts(user, sort, timeSorting, after, limit, host).body()
    }

    override suspend fun getUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return getRawUserComments(user, sort, timeSorting, after, limit, host).body()
    }

    override suspend fun searchPost(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return searchPostRaw(query, sort, timeSorting, after, limit, host).body()
    }

    override suspend fun searchUser(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return searchUserRaw(query, sort, timeSorting, after, limit, host).body()
    }

    override suspend fun searchSubreddit(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): Listing {
        return searchSubredditRaw(query, sort, timeSorting, after, limit, host).body()
    }
}
