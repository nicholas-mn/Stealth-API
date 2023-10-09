package com.cosmos.stealth.services.teddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
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
import com.cosmos.stealth.services.teddit.di.TedditModule.Qualifier.TEDDIT_QUALIFIER
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named(TEDDIT_QUALIFIER)
class DataTedditApi(
    @Named(TEDDIT_QUALIFIER) private val client: HttpClient,
    private val urlSubstitutor: UrlSubstitutor
) : TedditApi {

    override suspend fun getSubreddit(
        instance: String,
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val subredditParam = "subreddit" to subreddit
        val sortParam = "sort" to sort.type

        val url = urlSubstitutor.buildUrl(instance, GET_SUBREDDIT, subredditParam, sortParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("t", timeSorting?.type)
            parameter("after", after)
        }.body()
    }

    override suspend fun getSubredditInfo(instance: String, subreddit: String, host: String?): Child {
        val subredditParam = "subreddit" to subreddit

        val url = urlSubstitutor.buildUrl(instance, GET_SUBREDDIT_ABOUT, subredditParam)

        return client.get(url) {
            forward(host, host == null)
        }.body()
    }

    override suspend fun searchInSubreddit(
        instance: String,
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val subredditParam = "subreddit" to subreddit

        val url = urlSubstitutor.buildUrl(instance, GET_SEARCH_SUBREDDIT, subredditParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
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
        val permalinkParam = "permalink" to permalink

        val url = urlSubstitutor.buildUrl(instance, GET_POST, permalinkParam)

        suspend fun apiCall(url: String): HttpResponse {
            return client.get(url) {
                forward(host, host == null)

                parameter("limit", limit)
                parameter("sort", sort.type)
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
        val userParam = "user" to user

        val url = urlSubstitutor.buildUrl(instance, GET_USER_SUBMITTED, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("sort", sort.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
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
        val userParam = "user" to user

        val url = urlSubstitutor.buildUrl(instance, GET_USER_COMMENTS, userParam)

        return client.get(url) {
            forward(host, host == null)

            parameter("sort", sort.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
        }.body()
    }

    override suspend fun searchSubreddit(
        instance: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val url = urlSubstitutor.buildUrl(instance, GET_SEARCH_SR)

        return client.get(url) {
            forward(host, host == null)

            parameter("q", query)
            parameter("sort", sort?.type)
            parameter("t", timeSorting?.type)
            parameter("after", after)
        }.body()
    }
}
