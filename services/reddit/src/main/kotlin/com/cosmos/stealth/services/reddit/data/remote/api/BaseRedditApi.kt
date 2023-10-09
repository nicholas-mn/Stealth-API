package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
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
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

@Suppress("TooManyFunctions", "LongParameterList")
abstract class BaseRedditApi(private val client: HttpClient, private val urlSubstitutor: UrlSubstitutor) : RedditApi {

    protected suspend fun getRawSubreddit(
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        geoFilter: String?,
        host: String?
    ): HttpResponse {
        val subredditParam = "subreddit" to subreddit
        val sortParam = "sort" to sort.type

        val url = urlSubstitutor.buildUrl(GET_SUBREDDIT, subredditParam, sortParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
            parameter("geo_filter", geoFilter)
        }
    }

    protected suspend fun getRawSubredditInfo(subreddit: String, host: String?): HttpResponse {
        val subredditParam = "subreddit" to subreddit

        val url = urlSubstitutor.buildUrl(GET_SUBREDDIT_ABOUT, subredditParam)

        return client.get(url) {
            forward(host, host == null)
        }
    }

    protected suspend fun searchInSubredditRaw(
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        val subredditParam = "subreddit" to subreddit

        val url = urlSubstitutor.buildUrl(GET_SEARCH_SUBREDDIT, subredditParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }

    protected suspend fun getRawPost(permalink: String, limit: Int?, sort: Sort, host: String?): HttpResponse {
        val permalinkParam = "permalink" to permalink

        val url = urlSubstitutor.buildUrl(GET_POST, permalinkParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("limit", limit)
            parameter("sort", sort.type)
        }
    }

    protected suspend fun getRawMoreChildren(children: String, linkId: String, host: String?): HttpResponse {
        return client.get(GET_MORE_CHILDREN) {
            forward(host, host == null)

            parameter("children", children)
            parameter("link_id", linkId)
        }
    }

    protected suspend fun getRawUserInfo(user: String, host: String?): HttpResponse {
        val userParam = "user" to user

        val url = urlSubstitutor.buildUrl(GET_USER_ABOUT, userParam)

        return client.get(url) {
            forward(host, host == null)
        }
    }

    protected suspend fun getRawUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        val userParam = "user" to user

        val url = urlSubstitutor.buildUrl(GET_USER_SUBMITTED, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("sort", sort.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }

    protected suspend fun getRawUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        val userParam = "user" to user

        val url = urlSubstitutor.buildUrl(GET_USER_COMMENTS, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("sort", sort.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }

    protected suspend fun searchPostRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        return client.get(GET_SEARCH_LINK) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }

    protected suspend fun searchUserRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        return client.get(GET_SEARCH_USER) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }

    protected suspend fun searchSubredditRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        limit: Int?,
        host: String?
    ): HttpResponse {
        return client.get(GET_SEARCH_SR) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
            parameter("limit", limit)
        }
    }
}
