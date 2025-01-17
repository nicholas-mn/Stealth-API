package com.cosmos.stealth.services.reddit.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.Data
import com.cosmos.stealth.services.reddit.data.model.JsonMore
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.ListingData
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.TimeSorting
import com.cosmos.stealth.services.reddit.data.model.Token
import com.cosmos.stealth.services.reddit.data.scraper.CommentScraper
import com.cosmos.stealth.services.reddit.data.scraper.PostScraper
import com.cosmos.stealth.services.reddit.data.scraper.PostSearchScraper
import com.cosmos.stealth.services.reddit.data.scraper.SubredditScraper
import com.cosmos.stealth.services.reddit.data.scraper.SubredditSearchScraper
import com.cosmos.stealth.services.reddit.data.scraper.UserScaper
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Suppress("TooManyFunctions")
class ScrapRedditApi(
    client: HttpClient,
    urlSubstitutor: UrlSubstitutor,
    private val ioDispatcher: CoroutineDispatcher
) : BaseRedditApi(client, urlSubstitutor) {

    override val baseUrl: String
        get() = RedditApi.BASE_URL_OLD

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
        val response = getRawSubreddit(subreddit, sort, timeSorting, after, limit, geoFilter, null, host)
        return PostScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getSubredditInfo(subreddit: String, bearer: String?, host: String?): Child {
        val response = getRawSubreddit(subreddit, Sort.HOT, null, null, null, null, null, host)
        return SubredditScraper(ioDispatcher).scrap(response.bodyAsText())
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
        val response = searchInSubredditRaw(subreddit, query, sort, timeSorting, after, limit, null, host)
        return PostSearchScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getPost(
        permalink: String,
        limit: Int?,
        sort: Sort,
        bearer: String?,
        host: String?
    ): List<Listing> = supervisorScope {
        val response = getRawPost(permalink, limit, sort, null, host)
        val body = response.bodyAsText()

        val post = async {
            PostScraper(ioDispatcher).scrap(body)
        }

        val comments = async {
            CommentScraper(ioDispatcher).scrap(body)
        }

        listOf(post.await(), comments.await())
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
        val httpUrl = refLink?.toHttpUrlOrNull() ?: return MoreChildren(JsonMore(Data(emptyList())))

        // Get comment's parent ID
        val parent = parentId?.removePrefix("t1_").orEmpty()

        // Build the single thread URL
        val newUrl = httpUrl.newBuilder().addPathSegment(parent).build()

        // Build the permalink from the last 3 segments from the new URL (without the /comments/)
        val permalink = newUrl.pathSegments.takeLast(3).joinToString("/")

        val response = getRawPost(permalink, null, Sort.HOT, null, host)
        val body = response.bodyAsText()

        val comments = CommentScraper(ioDispatcher).scrap(body, depth?.minus(1) ?: 0)
        val listing = comments.data.children

        // Take only the replies from the first item as it is the item we want the replies for
        val replies = (listing.firstOrNull() as? CommentChild)
            ?.data
            ?.replies
            ?.data
            ?.children
            ?: listing

        return MoreChildren(JsonMore(Data(replies)))
    }

    override suspend fun getUserInfo(user: String, bearer: String?, host: String?): Child {
        val response = getRawUserPosts(user, Sort.HOT, null, null, null, null, host)
        return UserScaper(ioDispatcher).scrap(response.bodyAsText())
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
        val response = getRawUserPosts(user, sort, timeSorting, after, limit, null, host)
        return PostScraper(ioDispatcher).scrap(response.bodyAsText())
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
        val response = getRawUserComments(user, sort, timeSorting, after, limit, null, host)
        return CommentScraper(ioDispatcher).scrap(response.bodyAsText())
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
        val response = searchPostRaw(query, sort, timeSorting, after, limit, null, host)
        return PostSearchScraper(ioDispatcher).scrap(response.bodyAsText())
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
        // TODO
        return Listing("t2", ListingData(null, null, emptyList(), null, null))
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
        val response = searchSubredditRaw(query, sort, timeSorting, after, limit, null, host)
        return SubredditSearchScraper(ioDispatcher).scrap(response.bodyAsText())
    }

    override suspend fun getAccessToken(parameters: Parameters, bearer: String): Token {
        throw UnsupportedOperationException()
    }
}
