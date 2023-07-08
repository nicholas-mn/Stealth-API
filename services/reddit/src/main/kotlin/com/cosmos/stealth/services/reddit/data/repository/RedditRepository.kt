package com.cosmos.stealth.services.reddit.data.repository

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
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi
import okhttp3.HttpUrl.Companion.toHttpUrl

@Suppress("TooManyFunctions", "LongParameterList")
class RedditRepository(
    private val dataRedditApi: RedditApi,
    private val scrapRedditApi: RedditApi,
    postMapper: PostMapper,
    communityMapper: CommunityMapper,
    userMapper: UserMapper,
    commentMapper: CommentMapper
) : Repository(postMapper, communityMapper, userMapper, commentMapper) {

    override suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getSubreddit(subreddit, sorting.generalSorting, sorting.timeSorting, after, host = request.info.host)
        }

        return getSubreddit(response, request)
    }

    override suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo> {
        return getSubredditInfo(request) {
            getSource(request.service.instance).getSubredditInfo(subreddit, request.info.host)
        }
    }

    override suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchInSubreddit(request) {
            getSource(request.service.instance)
                .searchInSubreddit(
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
        val response = safeApiCall {
            getSource(request.service.instance)
                .getPost(permalink, limit, sort, request.info.host)
        }

        return getPost(response, request)
    }

    override suspend fun getMoreChildren(
        request: Request,
        children: List<String>,
        linkId: String
    ): Resource<List<Feedable>> {
        val childrenString = children.take(LOAD_MORE_LIMIT).joinToString(",")

        return getMoreChildren(request) { getSource(request.service.instance).getMoreChildren(childrenString, linkId) }
    }

    override suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo> {
        return getUserInfo(request) { getSource(request.service.instance).getUserInfo(user, request.info.host) }
    }

    override suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getUserPosts(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        return getUserPosts(response, request)
    }

    override suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed {
        val response = safeApiCall {
            getSource(request.service.instance)
                .getUserComments(user, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }

        return getUserComments(response, request)
    }

    override suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchPost(request) {
            getSource(request.service.instance)
                .searchPost(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    override suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchUser(request) {
            getSource(request.service.instance)
                .searchUser(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    override suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults> {
        return searchSubreddit(request) {
            getSource(request.service.instance)
                .searchSubreddit(query, sorting.generalSorting, sorting.timeSorting, after, request.info.host)
        }
    }

    private fun getSource(instance: String?): RedditApi {
        val instanceUrl = LinkValidator(instance).validUrl

        return when (instanceUrl?.host) {
            SCRAP_URL.host -> scrapRedditApi
            else -> dataRedditApi
        }
    }

    companion object {
        private const val LOAD_MORE_LIMIT = 100

        private val SCRAP_URL = RedditApi.BASE_URL_OLD.toHttpUrl()
    }
}
