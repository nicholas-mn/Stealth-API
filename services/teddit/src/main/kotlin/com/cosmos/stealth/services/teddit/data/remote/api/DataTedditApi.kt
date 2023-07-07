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
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

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
        val queryParam = TedditApi::searchInSubreddit.getPathParameter(1) to query

        val url = urlSubstitutor.buildUrl(instance, endpoint, subredditParam, queryParam)

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::searchInSubreddit.getQueryParameter(0), sort?.type)
            parameter(TedditApi::searchInSubreddit.getQueryParameter(1), timeSorting?.type)
            parameter(TedditApi::searchInSubreddit.getQueryParameter(2), after)
        }.body()
    }

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

        return client.get(url) {
            forward(host, host == null)

            parameter(TedditApi::getPost.getQueryParameter(0), limit)
            parameter(TedditApi::getPost.getQueryParameter(1), sort.type)
        }.body()
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
