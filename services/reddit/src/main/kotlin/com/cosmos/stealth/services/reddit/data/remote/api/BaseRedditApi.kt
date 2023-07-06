package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
import com.cosmos.stealth.core.network.util.getEndpoint
import com.cosmos.stealth.core.network.util.getPathParameter
import com.cosmos.stealth.core.network.util.getQueryParameter
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
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
        geoFilter: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::getSubreddit.getEndpoint()

        val subredditParam = RedditApi::getSubreddit.getPathParameter(0) to subreddit
        val sortParam = RedditApi::getSubreddit.getPathParameter(1) to sort.type

        val url = urlSubstitutor.buildUrl(endpoint, subredditParam, sortParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(RedditApi::getSubreddit.getQueryParameter(0), timeSorting?.type)
            parameter(RedditApi::getSubreddit.getQueryParameter(1), after)
            parameter(RedditApi::getSubreddit.getQueryParameter(2), geoFilter)
        }
    }

    protected suspend fun getRawSubredditInfo(subreddit: String, host: String?): HttpResponse {
        val endpoint = RedditApi::getSubredditInfo.getEndpoint()

        val subredditParam = RedditApi::getSubredditInfo.getPathParameter(0) to subreddit

        val url = urlSubstitutor.buildUrl(endpoint, subredditParam)

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
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::searchInSubreddit.getEndpoint()

        val subredditParam = RedditApi::searchInSubreddit.getPathParameter(0) to subreddit
        val queryParam = RedditApi::searchInSubreddit.getPathParameter(1) to query

        val url = urlSubstitutor.buildUrl(endpoint, subredditParam, queryParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(RedditApi::searchInSubreddit.getQueryParameter(0), sort?.type)
            parameter(RedditApi::searchInSubreddit.getQueryParameter(1), timeSorting?.type)
            parameter(RedditApi::searchInSubreddit.getQueryParameter(2), after)
        }
    }

    protected suspend fun getRawPost(permalink: String, limit: Int?, sort: Sort, host: String?): HttpResponse {
        val endpoint = RedditApi::getPost.getEndpoint()

        val permalinkParam = RedditApi::getPost.getPathParameter(0) to permalink

        val url = urlSubstitutor.buildUrl(endpoint, permalinkParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(RedditApi::getPost.getQueryParameter(0), limit)
            parameter(RedditApi::getPost.getQueryParameter(1), sort.type)
        }
    }

    protected suspend fun getRawMoreChildren(children: String, linkId: String, host: String?): HttpResponse {
        val endpoint = RedditApi::getMoreChildren.getEndpoint()

        return client.get(endpoint) {
            forward(host, host == null)

            parameter(RedditApi::getMoreChildren.getQueryParameter(0), children)
            parameter(RedditApi::getMoreChildren.getQueryParameter(1), linkId)
        }
    }

    protected suspend fun getRawUserInfo(user: String, host: String?): HttpResponse {
        val endpoint = RedditApi::getUserInfo.getEndpoint()

        val userParam = RedditApi::getUserInfo.getPathParameter(0) to user

        val url = urlSubstitutor.buildUrl(endpoint, userParam)

        return client.get(url) {
            forward(host, host == null)
        }
    }

    protected suspend fun getRawUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::getUserPosts.getEndpoint()

        val userParam = RedditApi::getUserPosts.getPathParameter(0) to user

        val url = urlSubstitutor.buildUrl(endpoint, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(RedditApi::getUserPosts.getQueryParameter(0), sort.type)
            parameter(RedditApi::getUserPosts.getQueryParameter(1), timeSorting?.type)
            parameter(RedditApi::getUserPosts.getQueryParameter(2), after)
        }
    }

    protected suspend fun getRawUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::getUserComments.getEndpoint()

        val userParam = RedditApi::getUserComments.getPathParameter(0) to user

        val url = urlSubstitutor.buildUrl(endpoint, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(RedditApi::getUserComments.getQueryParameter(0), sort.type)
            parameter(RedditApi::getUserComments.getQueryParameter(1), timeSorting?.type)
            parameter(RedditApi::getUserComments.getQueryParameter(2), after)
        }
    }

    @Suppress("MagicNumber")
    protected suspend fun searchPostRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::searchPost.getEndpoint()

        return client.get(endpoint) {
            forward(host, host == null)

            parameter(RedditApi::searchPost.getQueryParameter(0), query)
            parameter(RedditApi::searchPost.getQueryParameter(1), sort?.type)
            parameter(RedditApi::searchPost.getQueryParameter(2), timeSorting?.type)
            parameter(RedditApi::searchPost.getQueryParameter(3), after)
        }
    }

    @Suppress("MagicNumber")
    protected suspend fun searchUserRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::searchUser.getEndpoint()

        return client.get(endpoint) {
            forward(host, host == null)

            parameter(RedditApi::searchUser.getQueryParameter(0), query)
            parameter(RedditApi::searchUser.getQueryParameter(1), sort?.type)
            parameter(RedditApi::searchUser.getQueryParameter(2), timeSorting?.type)
            parameter(RedditApi::searchUser.getQueryParameter(3), after)
        }
    }

    @Suppress("MagicNumber")
    protected suspend fun searchSubredditRaw(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): HttpResponse {
        val endpoint = RedditApi::searchSubreddit.getEndpoint()

        return client.get(endpoint) {
            forward(host, host == null)

            parameter(RedditApi::searchSubreddit.getQueryParameter(0), query)
            parameter(RedditApi::searchSubreddit.getQueryParameter(1), sort?.type)
            parameter(RedditApi::searchSubreddit.getQueryParameter(2), timeSorting?.type)
            parameter(RedditApi::searchSubreddit.getQueryParameter(3), after)
        }
    }
}
