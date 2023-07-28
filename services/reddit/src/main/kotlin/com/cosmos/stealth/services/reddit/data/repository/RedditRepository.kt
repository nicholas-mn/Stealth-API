package com.cosmos.stealth.services.reddit.data.repository

import com.cosmos.stealth.core.model.api.Appendable
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.util.LinkValidator
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.remote.api.DataRedditApi
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi
import com.cosmos.stealth.services.reddit.data.remote.api.ScrapRedditApi
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("TooManyFunctions", "LongParameterList")
class RedditRepository(
    private val dataRedditApi: RedditApi,
    private val scrapRedditApi: RedditApi,
    postMapper: PostMapper,
    communityMapper: CommunityMapper,
    userMapper: UserMapper,
    commentMapper: CommentMapper,
    defaultDispatcher: CoroutineDispatcher
) : Repository(postMapper, communityMapper, userMapper, commentMapper, defaultDispatcher) {

    override suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed {
        val source = getSource(request.service.instance)

        val response = safeApiCall {
            source.getSubreddit(subreddit, sorting.generalSorting, sorting.timeSorting, after, host = request.info.host)
        }

        return getSubreddit(response, source.getRequest(request))
    }

    override suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo> {
        val source = getSource(request.service.instance)

        return getSubredditInfo(source.getRequest(request)) {
            source.getSubredditInfo(subreddit, request.info.host)
        }
    }

    override suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        val source = getSource(request.service.instance)

        return searchInSubreddit(source.getRequest(request)) {
            source.searchInSubreddit(
                subreddit,
                query,
                sorting.generalSorting,
                sorting.timeSorting,
                after,
                request.info.host
            )
        }
    }

    override suspend fun getPost(request: Request, permalink: String, limit: Int?, sort: Sort): Resource<Post> {
        val source = getSource(request.service.instance)

        val response = safeApiCall {
            source.getPost(permalink, limit, sort, request.info.host)
        }

        return getPost(response, source.getRequest(request))
    }

    override suspend fun getMoreChildren(
        request: Request,
        appendable: Appendable
    ): Resource<List<Feedable>> {
        val containsMoreComments = appendable.content.size > LOAD_MORE_LIMIT

        val children = appendable.content.take(LOAD_MORE_LIMIT).joinToString(",")

        var additionalContentFeedable: Appendable? = null

        if (containsMoreComments) {
            val count = appendable.count

            // Remove first 100 comments from list
            val content = appendable.content.toMutableList()
                .apply { subList(0, LOAD_MORE_LIMIT).clear() }

            additionalContentFeedable = appendable.copy(
                count = if (count > LOAD_MORE_LIMIT) count - LOAD_MORE_LIMIT else 0,
                content = content.toList()
            )
        }

        val source = getSource(request.service.instance)

        return getMoreChildren(source.getRequest(request), appendable, additionalContentFeedable) {
            source.getMoreChildren(children, appendable.parentId)
        }
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        val source = getSource(request.service.instance)
        return getUserInfo(source.getRequest(request)) { source.getUserInfo(user, request.info.host) }
    }

    override suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val source = getSource(request.service.instance)
        val response = safeApiCall {
            source.getUserPosts(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        return getUserPosts(response, source.getRequest(request))
    }

    override suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val source = getSource(request.service.instance)
        val response = safeApiCall {
            source.getUserComments(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        return getUserComments(response, source.getRequest(request))
    }

    override suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        val source = getSource(request.service.instance)
        return searchPost(source.getRequest(request)) {
            source.searchPost(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    override suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        val source = getSource(request.service.instance)
        return searchUser(source.getRequest(request)) {
            source.searchUser(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    override suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        val source = getSource(request.service.instance)
        return searchSubreddit(source.getRequest(request)) {
            source.searchSubreddit(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    private fun getSource(instance: String?): RedditApi {
        val instanceUrl = LinkValidator(instance).validUrl

        return when (instanceUrl?.host) {
            SCRAP_URL.host -> scrapRedditApi
            else -> dataRedditApi
        }
    }

    private fun RedditApi.getRequest(request: Request): Request {
        return when (this) {
            is DataRedditApi -> request.copy(service = request.service.copy(instance = BASE_URL.host))
            is ScrapRedditApi -> request.copy(service = request.service.copy(instance = SCRAP_URL.host))
            else -> request
        }
    }

    companion object {
        private const val LOAD_MORE_LIMIT = 100

        private val BASE_URL = RedditApi.BASE_URL.toHttpUrl()
        private val SCRAP_URL = RedditApi.BASE_URL_OLD.toHttpUrl()
    }
}
