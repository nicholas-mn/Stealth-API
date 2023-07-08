package com.cosmos.stealth.services.teddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
import com.cosmos.stealth.core.network.util.getEndpoint
import com.cosmos.stealth.core.network.util.getPathParameter
import com.cosmos.stealth.core.network.util.getQueryParameter
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.teddit.data.model.TedditUser
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request

class DataTedditApi(private val client: HttpClient, private val urlSubstitutor: UrlSubstitutor) : TedditApi {

    override suspend fun getSubreddit(
        instance: String,
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val endpoint = TedditApi::getSubreddit.getEndpoint()

        val subredditParam = TedditApi::getSubreddit.getPathParameter(0) to subreddit
        val sortParam = TedditApi::getSubreddit.getPathParameter(1) to sort.type

        val url = urlSubstitutor.buildUrl(instance, endpoint, subredditParam, sortParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::getSubreddit.getQueryParameter(0), timeSorting?.type)
            parameter(TedditApi::getSubreddit.getQueryParameter(1), after)
        }.body()
    }

    override suspend fun getSubredditInfo(instance: String, subreddit: String, host: String?): Child {
        val endpoint = TedditApi::getSubredditInfo.getEndpoint()

        val subredditParam = TedditApi::getSubredditInfo.getPathParameter(0) to subreddit

        val url = urlSubstitutor.buildUrl(instance, endpoint, subredditParam)

        return client.get(url) {
            forward(host, host == null)
        }.body()
    }

    @Suppress("MagicNumber")
    override suspend fun searchInSubreddit(
        instance: String,
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val endpoint = TedditApi::searchInSubreddit.getEndpoint()

        val subredditParam = TedditApi::searchInSubreddit.getPathParameter(0) to subreddit

        val url = urlSubstitutor.buildUrl(instance, endpoint, subredditParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::searchInSubreddit.getQueryParameter(0), query)
            parameter(TedditApi::searchInSubreddit.getQueryParameter(1), sort?.type)
            parameter(TedditApi::searchInSubreddit.getQueryParameter(2), timeSorting?.type)
            parameter(TedditApi::searchInSubreddit.getQueryParameter(3), after)
        }.body()
    }

    @Suppress("SwallowedException")
    override suspend fun getPost(
        instance: String,
        permalink: String,
        limit: Int?,
        sort: Sort,
        host: String?
    ): List<Listing> {
        val endpoint = TedditApi::getPost.getEndpoint()

        val permalinkParam = TedditApi::getPost.getPathParameter(0) to permalink

        val url = urlSubstitutor.buildUrl(instance, endpoint, permalinkParam)

        suspend fun apiCall(url: String): HttpResponse {
            return client.get(url) {
                forward(host, host == null)

                parameter(TedditApi::getPost.getQueryParameter(0), limit)
                parameter(TedditApi::getPost.getQueryParameter(1), sort.type)
            }
        }

        val response = apiCall(url)

        return try {
            response.body<List<Listing>>()
        } catch (e: NoTransformationFoundException) {
            // Workaround for Teddit not keeping query parameters on redirections.
            // Requests with only the post ID will redirect to the full URL before being processed, e.g.
            // https://teddit.net/14u4w33 ->
            // https://teddit.net/r/privacy/comments/14u4w33/how_threads_privacy_policy_compares_to_twitters/.
            // It means that the api parameter (along with other query parameters) will be lost during the redirection,
            // making the response un-parseable (since it is an HTML page).
            // Here, the redirect URL from the response is used to make another call to the right endpoint and thus
            // getting the API response.
            val redirectUrl = response.request.url.toString().plus("?api")
            apiCall(redirectUrl).body()
        }
    }

    override suspend fun getUserPosts(
        instance: String,
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): TedditUser {
        val endpoint = TedditApi::getUserPosts.getEndpoint()

        val userParam = TedditApi::getUserPosts.getPathParameter(0) to user

        val url = urlSubstitutor.buildUrl(instance, endpoint, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::getUserPosts.getQueryParameter(0), sort.type)
            parameter(TedditApi::getUserPosts.getQueryParameter(1), timeSorting?.type)
            parameter(TedditApi::getUserPosts.getQueryParameter(2), after)
        }.body()
    }

    override suspend fun getUserComments(
        instance: String,
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): TedditUser {
        val endpoint = TedditApi::getUserComments.getEndpoint()

        val userParam = TedditApi::getUserComments.getPathParameter(0) to user

        val url = urlSubstitutor.buildUrl(instance, endpoint, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::getUserComments.getQueryParameter(0), sort.type)
            parameter(TedditApi::getUserComments.getQueryParameter(1), timeSorting?.type)
            parameter(TedditApi::getUserComments.getQueryParameter(2), after)
        }.body()
    }

    @Suppress("MagicNumber")
    override suspend fun searchSubreddit(
        instance: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val endpoint = TedditApi::searchSubreddit.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::searchSubreddit.getQueryParameter(0), query)
            parameter(TedditApi::searchSubreddit.getQueryParameter(1), sort?.type)
            parameter(TedditApi::searchSubreddit.getQueryParameter(2), timeSorting?.type)
            parameter(TedditApi::searchSubreddit.getQueryParameter(3), after)
        }.body()
    }
}
