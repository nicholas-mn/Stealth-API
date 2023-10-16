package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.reddit.data.model.Token
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Companion.BASE_URL
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi.Endpoint.POST_ACCESS_TOKEN
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters

@Suppress("TooManyFunctions")
class DataRedditApi(client: HttpClient, urlSubstitutor: UrlSubstitutor) : BaseRedditApi(client, urlSubstitutor) {

    override val baseUrl: String
        get() = BASE_URL

    override suspend fun getSubreddit(
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        geoFilter: String?,
        bearer: String?,
        host: String?
    ): Listing {
        return getRawSubreddit(subreddit, sort, timeSorting, after, limit, geoFilter, bearer, host).body()
    }

    override suspend fun getSubredditInfo(subreddit: String, bearer: String?, host: String?): Child {
        return getRawSubredditInfo(subreddit, bearer, host).body()
    }

    override suspend fun searchInSubreddit(
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return searchInSubredditRaw(subreddit, query, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun getPost(
        permalink: String,
        limit: Int?,
        sort: Sort,
        bearer: String?,
        host: String?
    ): List<Listing> {
        return getRawPost(permalink, limit, sort, bearer, host).body()
    }

    override suspend fun getMoreChildren(
        children: String,
        linkId: String,
        bearer: String?,
        host: String?,
        parentId: String?,
        refLink: String?,
        depth: Int?
    ): MoreChildren {
        return getRawMoreChildren(children, linkId, bearer, host).body()
    }

    override suspend fun getUserInfo(user: String, bearer: String?, host: String?): Child {
        return getRawUserInfo(user, bearer, host).body()
    }

    override suspend fun getUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return getRawUserPosts(user, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun getUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return getRawUserComments(user, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun searchPost(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return searchPostRaw(query, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun searchUser(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return searchUserRaw(query, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun searchSubreddit(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        bearer: String?,
        host: String?
    ): Listing {
        return searchSubredditRaw(query, sort, timeSorting, after, limit, bearer, host).body()
    }

    override suspend fun getAccessToken(parameters: Parameters, bearer: String): Token {
        val url = urlSubstitutor.buildUrl(BASE_URL, POST_ACCESS_TOKEN)

        return client.submitForm(url, parameters) {
            header(HttpHeaders.Authorization, "Basic $bearer")
        }.body()
    }
}
