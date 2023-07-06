package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.Data
import com.cosmos.stealth.services.reddit.data.model.JsonMore
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.ListingData
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.reddit.data.scraper.CommentScraper
import com.cosmos.stealth.services.reddit.data.scraper.PostScraper
import com.cosmos.stealth.services.reddit.data.scraper.SubredditScraper
import com.cosmos.stealth.services.reddit.data.scraper.SubredditSearchScraper
import com.cosmos.stealth.services.reddit.data.scraper.UserScaper
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

@Suppress("TooManyFunctions")
class ScrapRedditApi(
    client: HttpClient,
    urlSubstitutor: UrlSubstitutor,
    mainImmediateDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : BaseRedditApi(client, urlSubstitutor) {

    private val scope = CoroutineScope(mainImmediateDispatcher + SupervisorJob())

    override suspend fun getSubreddit(
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        geoFilter: String?,
        host: String?
    ): Listing {
        val response = getRawSubreddit(subreddit, sort, timeSorting, after, geoFilter, host)
        return PostScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getSubredditInfo(subreddit: String, host: String?): Child {
        val response = getRawSubreddit(subreddit, Sort.HOT, null, null, null, host)
        return SubredditScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun searchInSubreddit(
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        // TODO
        return Listing("t3", ListingData(null, null, emptyList(), null, null))
    }

    override suspend fun getPost(permalink: String, limit: Int?, sort: Sort, host: String?): List<Listing> {
        val response = getRawPost(permalink, limit, sort, host)
        val body = response.bodyAsText()

        val post = scope.async {
            PostScraper(ioDispatcher).scrap(body)
        }

        val comments = scope.async {
            CommentScraper(ioDispatcher).scrap(body)
        }

        return listOf(post.await(), comments.await())
    }

    override suspend fun getMoreChildren(children: String, linkId: String, host: String?): MoreChildren {
        // TODO
        return MoreChildren(JsonMore(Data(emptyList())))
    }

    override suspend fun getUserInfo(user: String, host: String?): Child {
        val response = getRawUserPosts(user, Sort.HOT, null, null, host)
        return UserScaper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val response = getRawUserPosts(user, sort, timeSorting, after, host)
        return PostScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val response = getRawUserComments(user, sort, timeSorting, after, host)
        return CommentScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun searchPost(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        // TODO
        return Listing("t3", ListingData(null, null, emptyList(), null, null))
    }

    override suspend fun searchUser(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        // TODO
        return Listing("t2", ListingData(null, null, emptyList(), null, null))
    }

    override suspend fun searchSubreddit(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?,
        host: String?
    ): Listing {
        val response = searchSubredditRaw(query, sort, timeSorting, after, host)
        return SubredditSearchScraper(ioDispatcher).scrap(response.bodyAsText())
    }
}
